/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.NguoiDung;
import com.ndk.services.NguoiDungService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Controller
public class NguoiDungController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String username = principal.getName();
        NguoiDung user = nguoiDungService.getByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để đổi mật khẩu.");
            return "redirect:/login";
        }

        String username = principal.getName();
        NguoiDung user = nguoiDungService.getByUsername(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại.");
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng");
            return "redirect:/profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp");
            return "redirect:/profile";
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự");
            return "redirect:/profile";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        nguoiDungService.mergeUser(user);

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        return "redirect:/profile";
    }
}
