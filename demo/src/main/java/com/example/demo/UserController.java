package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private WebsiteRepository websiteRepository; // 添加一个 JPA 仓库，用于访问数据库
// http://localhost:8080/user?userId=1&additionalInfo=2
    @GetMapping("/user")
    public String getUser(@RequestParam String userId, @RequestParam String additionalInfo) {
        return userInterface.getUserInfo(userId, additionalInfo);
    }
// http://localhost:8080/website?id=1
    @GetMapping("/website")
    public String getWebsite(@RequestParam Integer id) {  // 使用 Integer
        System.out.println("Received ID: " + id);
        Optional<Website> website = websiteRepository.findById(id);  // 修正拼写错误
        System.out.println("Database Query Result: " + website);
        return website.map(w -> "id: " + w.getId() + " Name: " + w.getName() +
                        ", URL: " + w.getUrl() +
                        ", alexa: " + w.getAlexa() +
                        ", country: " + w.getCountry())  // 修正 alexa 重复问题
                .orElse("Website not found");
    }

    public static class ApiResponse {
        private int statusCode;
        private String message;
        private List<Website> data;

        public ApiResponse(int statusCode, String message, List<Website> data) {
            this.statusCode = statusCode;
            this.message = message;
            this.data = data;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }

        public List<Website> getData() {
            return data;
        }
    }

    // http://localhost:8080/websites
    @GetMapping("/websites")
    public ApiResponse getAllWebsites() {
        try {
            List<Website> websites = websiteRepository.findAll();
            System.out.println("Retrieved all websites: " + websites.size() + " records");
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
            Website savedWebsite = websiteRepository.save(website); // 此时 `id` 会自动生成
            System.out.println("Created website with ID: " + savedWebsite.getId());
            return new ApiResponse(201, "Website created successfully", Arrays.asList(savedWebsite));
        } catch (Exception e) {
            System.out.println("Error creating website: " + e.getMessage());
            return new ApiResponse(500, "Error creating website: " + e.getMessage(), null);
        }
    }

    // 新增接口：删除一个网站
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

    // 新增接口：编辑一个网站
    @PutMapping("/website/{id}")
    public ApiResponse updateWebsite(@PathVariable Integer id, @RequestBody Website website) {
        try {
            Optional<Website> optionalWebsite = websiteRepository.findById(id);
            if (optionalWebsite.isPresent()) {
                Website existingWebsite = optionalWebsite.get();
                // 更新网站信息
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
