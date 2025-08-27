/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.NguoiDung;
import com.ndk.services.MoHinhService;
import com.ndk.services.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Controller
public class IndexController {
    @Autowired
    private MoHinhService sanPhamService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @RequestMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            NguoiDung user = nguoiDungService.getByUsername(username);

            if (user != null) {
                String role = user.getVaiTro().trim();

                if (role.equalsIgnoreCase("ROLE_ADMIN")) {
                    System.out.println("Redirecting to /admin");
                    return "redirect:/admin";
                }
                else {
                    // đăng nhập đúng nhưng không có quyền → đá về login + thông báo
                    return "redirect:/login?notAdmin=true";
                }
            }
            System.out.println("Redirecting user with role: " + user.getVaiTro());
            System.out.println("Login user: " + authentication.getName());
            System.out.println("Role: " + user.getVaiTro());
        }

        // Nếu không đăng nhập thì vẫn hiển thị danh sách sản phẩm
        model.addAttribute("sanPhams", sanPhamService.getAllSanPham());
        return "index";
    }
}
