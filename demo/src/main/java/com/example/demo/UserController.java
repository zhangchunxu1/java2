package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ==================== 登录注册相关 ====================

    // 注册接口
    @PostMapping("/register")
    public ApiResponse register(@RequestBody RegisterRequest registerRequest) {
        try {
            // 检查用户名是否已存在
            if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
                return new ApiResponse(400, "用户名已存在", null);
            }
            // 创建新用户，密码加密存储
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            userRepository.save(user);
            return new ApiResponse(200, "注册成功", null);
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            return new ApiResponse(500, "注册失败: " + e.getMessage(), null);
        }
    }

    // 登录接口 - 返回JWT Token
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
            if (!userOptional.isPresent()) {
                return new ApiResponse(401, "用户名不存在", null);
            }
            User user = userOptional.get();
            // 使用BCrypt验证密码
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // 生成JWT Token
                String token = jwtUtil.generateToken(user.getUsername());
                return new ApiResponse(200, "登录成功", null, token, user.getUsername());
            } else {
                return new ApiResponse(401, "密码错误", null);
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            return new ApiResponse(500, "登录出错: " + e.getMessage(), null);
        }
    }

    // 登录请求实体类
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // 注册请求实体类
    public static class RegisterRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // ==================== 网站管理相关 ====================

    @Autowired
    private WebsiteRepository websiteRepository;

    // http://localhost:8080/user?userId=1&additionalInfo=2
    @GetMapping("/user")
    public String getUser(@RequestParam String userId, @RequestParam String additionalInfo) {
        return userInterface.getUserInfo(userId, additionalInfo);
    }

    // http://localhost:8080/website?id=1
    @GetMapping("/website")
    public String getWebsite(@RequestParam Integer id) {
        System.out.println("Received ID: " + id);
        Optional<Website> website = websiteRepository.findById(id);
        System.out.println("Database Query Result: " + website);
        return website.map(w -> "id: " + w.getId() + " Name: " + w.getName() +
                        ", URL: " + w.getUrl() +
                        ", alexa: " + w.getAlexa() +
                        ", country: " + w.getCountry())
                .orElse("Website not found");
    }

    public static class ApiResponse {
        private int statusCode;
        private String message;
        private List<Website> data;
        private String token;
        private String username;

        public ApiResponse(int statusCode, String message, List<Website> data) {
            this(statusCode, message, data, null, null);
        }

        public ApiResponse(int statusCode, String message, List<Website> data, String token, String username) {
            this.statusCode = statusCode;
            this.message = message;
            this.data = data;
            this.token = token;
            this.username = username;
        }

        public int getStatusCode() { return statusCode; }
        public String getMessage() { return message; }
        public List<Website> getData() { return data; }
        public String getToken() { return token; }
        public String getUsername() { return username; }
    }

    // http://localhost:8080/websites
    @GetMapping("/websites")
    public ApiResponse getAllWebsites(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String country,
                                      @RequestParam(required = false) Integer alexa) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Website> websitePage;
            if (name != null && country != null && alexa != null) {
                websitePage = websiteRepository.findByNameContainingAndCountryContainingAndAlexa(name, country, alexa, pageable);
            } else if (name != null && country != null) {
                websitePage = websiteRepository.findByNameContainingAndCountryContaining(name, country, pageable);
            } else if (name != null && alexa != null) {
                websitePage = websiteRepository.findByNameContainingAndAlexa(name, alexa, pageable);
            } else if (country != null && alexa != null) {
                websitePage = websiteRepository.findByCountryContainingAndAlexa(country, alexa, pageable);
            } else if (name != null) {
                websitePage = websiteRepository.findByNameContaining(name, pageable);
            } else if (country != null) {
                websitePage = websiteRepository.findByCountryContaining(country, pageable);
            } else if (alexa != null) {
                websitePage = websiteRepository.findByAlexa(alexa, pageable);
            } else {
                websitePage = websiteRepository.findAll(pageable);
            }
            List<Website> websites = websitePage.getContent();
            System.out.println("Retrieved websites: " + websites.size() + " records on page " + page);
            return new ApiResponse(200, "Success", websites);
        } catch (Exception e) {
            System.out.println("Error retrieving all websites: " + e.getMessage());
            return new ApiResponse(500, "Error retrieving all websites: " + e.getMessage(), null);
        }
    }

    // 新增接口：添加一个网站
    @PostMapping("/website")
    public ApiResponse createWebsite(@RequestBody Website website) {
        try {
            Website savedWebsite = websiteRepository.save(website);
            System.out.println("Created website with ID: " + savedWebsite.getId());
            return new ApiResponse(201, "Website created successfully", Arrays.asList(savedWebsite));
        } catch (Exception e) {
            System.out.println("Error creating website: " + e.getMessage());
            return new ApiResponse(500, "Error creating website: " + e.getMessage(), null);
        }
    }

    // 删除接口
    @DeleteMapping("/website/{id}")
    public ApiResponse deleteWebsite(@PathVariable Integer id) {
        try {
            websiteRepository.deleteById(id);
            System.out.println("Deleted website with ID: " + id);
            return new ApiResponse(200, "Website deleted successfully", null);
        } catch (Exception e) {
            System.out.println("Error deleting website with ID: " + id + ", Error: " + e.getMessage());
            return new ApiResponse(500, "Error deleting website: " + e.getMessage(), null);
        }
    }

    // 导出接口
    @GetMapping("/export/websites")
    public void exportWebsites(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"websites.csv\"");
        OutputStream outputStream = response.getOutputStream();
        CSVWriter writer = new CSVWriter(new java.io.OutputStreamWriter(outputStream));
        String[] headers = {"ID", "Name", "URL", "Alexa", "Country"};
        writer.writeNext(headers);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Website> websitePage = websiteRepository.findAll(pageable);
        List<Website> websites = websitePage.getContent();
        for (Website website : websites) {
            String[] row = {
                    String.valueOf(website.getId()),
                    website.getName(),
                    website.getUrl(),
                    String.valueOf(website.getAlexa()),
                    website.getCountry()
            };
            writer.writeNext(row);
        }
        writer.flush();
        writer.close();
        outputStream.close();
    }

    // 编辑接口
    @PutMapping("/website/{id}")
    public ApiResponse updateWebsite(@PathVariable Integer id, @RequestBody Website website) {
        try {
            Optional<Website> optionalWebsite = websiteRepository.findById(id);
            if (optionalWebsite.isPresent()) {
                Website existingWebsite = optionalWebsite.get();
                existingWebsite.setName(website.getName());
                existingWebsite.setUrl(website.getUrl());
                existingWebsite.setAlexa(website.getAlexa());
                existingWebsite.setCountry(website.getCountry());
                Website updatedWebsite = websiteRepository.save(existingWebsite);
                System.out.println("Updated website with ID: " + updatedWebsite.getId());
                return new ApiResponse(200, "Website updated successfully", Arrays.asList(updatedWebsite));
            } else {
                return new ApiResponse(404, "Website not found", null);
            }
        } catch (Exception e) {
            System.out.println("Error updating website with ID: " + id + ", Error: " + e.getMessage());
            return new ApiResponse(500, "Error updating website: " + e.getMessage(), null);
        }
    }
}
