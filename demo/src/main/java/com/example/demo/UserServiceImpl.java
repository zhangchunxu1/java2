package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserInterface {

    @Override
    public String getUserInfo(String userId, String additionalInfo) {
        // 在这里实现获取用户信息的逻辑
        // 使用 userId 和 additionalInfo 参数
        return "用户信息"; // 示例返回值
    }
}