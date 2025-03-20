package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return website.map(w ->"id: " + w.getId() +  "Name: " + w.getName() + 
                              ", URL: " + w.getUrl() + 
                              ", alexa: " + w.getAlexa() + 
                              ", alexa: " + w.getCountry() 
                            )
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

        public int getStatusCode() { return statusCode; }
        public String getMessage() { return message; }
        public List<Website> getData() { return data; }
    }
//http://localhost:8080/websites
    @GetMapping("/websites")
    public ApiResponse getAllWebsites() {
        List<Website> websites = websiteRepository.findAll();
        System.out.println("Retrieved all websites: " + websites.size() + " records");
        return new ApiResponse(200, "Success", websites);
    }
    
    
    
    
    
}
