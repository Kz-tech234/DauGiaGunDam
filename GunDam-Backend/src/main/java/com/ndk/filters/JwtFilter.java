/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.filters;

import com.ndk.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 *
 * @author Tran Quoc Phong
 */
public class JwtFilter implements Filter{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 0) BỎ QUA preflight CORS
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String contextPath = httpRequest.getContextPath();   // ví dụ: /SpringDauGiaMoHinh
        String uri = httpRequest.getRequestURI();            // ví dụ: /SpringDauGiaMoHinh/api/phiendaugianguoidung/lich-su/5

        // 1) CHO PHÉP PUBLIC: /api/phiendaugianguoidung/lich-su/**
        if (uri.startsWith(contextPath + "/api/phiendaugianguoidung/lich-su/")
            || uri.startsWith(contextPath + "/api/taikhoannganhang/nguoiban/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2) Chỉ kiểm tra token với các nhóm path cần bảo vệ
        if (uri.contains("/api/secure")
                || uri.contains("/api/sanpham")
                || uri.contains("/api/phiendaugianguoidung")
                || uri.contains("/api/theodoisanpham")
                || uri.contains("/api/thanhtoan")
                || uri.contains("/api/taikhoannganhang")
                || uri.contains("/api/phiendaugia/baidau")
                || uri.contains("/api/phiendaugia/bai-dau-gia")) {

            String header = httpRequest.getHeader("Authorization");
            System.out.println("[JWT] URI=" + uri + " | Header=" + header);

            if (header == null || !header.startsWith("Bearer ")) {
                System.out.println("[JWT] -> NO BEARER -> 401");
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            }

            String token = header.substring(7);
            try {
                String username = JwtUtils.validateTokenAndGetUsername(token);
                if (username != null) {
                    System.out.println("[JWT] -> OK user=" + username + " | " + uri);
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, java.util.Collections.emptyList());
                    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                    return;
                } else {
                    System.out.println("[JWT] -> INVALID/EXPIRED -> 401");
                }
            } catch (Exception e) {
                System.out.println("[JWT] -> EXCEPTION " + e.getMessage());
            }
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc hết hạn");
            return;
        }

        // 3) Các request còn lại bỏ qua filter
        chain.doFilter(request, response);
    }
}
