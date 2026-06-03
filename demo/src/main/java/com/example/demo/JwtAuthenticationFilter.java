package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // 不需要鉴权的路径白名单
    private static final String[] WHITE_LIST = {
            "/login",
            "/register"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 设置CORS响应头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");

        // 浏览器OPTIONS预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String requestPath = request.getRequestURI();

        // 白名单路径直接放行
        for (String path : WHITE_LIST) {
            if (requestPath.equals(path)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 获取请求头中的Token
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                if (!jwtUtil.isTokenExpired(token)) {
                    String username = jwtUtil.getUsernameFromToken(token);
                    request.setAttribute("currentUser", username);
                    chain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                // Token解析失败
            }
        }

        // 未登录或Token无效，返回401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"statusCode\":401,\"message\":\"未登录或登录已过期，请重新登录\"}");
    }
}
