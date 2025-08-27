/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.NguoiDung;
import com.ndk.services.NguoiDungService;
import com.ndk.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiNguoiDungController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(this.nguoiDungService.getAllUsers());
    }
    @PostMapping("/users")
    public ResponseEntity<NguoiDung> create(@RequestParam Map<String, String> params,
            @RequestParam(name = "avatar", required = false) MultipartFile avatar) {
        NguoiDung u = this.nguoiDungService.addUser(params, avatar);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody NguoiDung u) {
        if (this.nguoiDungService.authenticate(u.getUsername(), u.getPassword())) {
            try {

                // Lấy người dùng từ service
                NguoiDung user = nguoiDungService.getByUsername(u.getUsername());
                // Kiểm tra trạng thái người dùng
                if (!"DUOC_DUYET".equals(user.getTrangThai())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Tài khoản chưa được duyệt.");
                }

                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam Map<String, String> params,
            @RequestParam(name = "avatar", required = false) MultipartFile avatar) {
        // Đảm bảo username và password tồn tại
        if (!params.containsKey("username") || !params.containsKey("password")) {
            return ResponseEntity.badRequest().body("Thiếu thông tin đăng ký");
        }

        // Gán trạng thái mặc định là CHO_DUYET
        params.put("trangThai", "CHO_DUYET");

        // Gán vai trò mặc định là ROLE_USER nếu chưa có
        if (!params.containsKey("vaiTro")) {
            params.put("vaiTro", "ROLE_NGUOIMUA");
        }

        // Gọi service để thêm người dùng
        try {
            NguoiDung newUser = nguoiDungService.addUser(params, avatar);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đăng ký thất bại: " + e.getMessage());
        }
    }

    @RequestMapping("/secure/profile")
    @ResponseBody
    public ResponseEntity<?> getProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        NguoiDung user = this.nguoiDungService.getByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/secure/change-password")
    public ResponseEntity<?> changePasswordApi(
            @RequestBody Map<String, String> payload,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập.");
        }

        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");
        String confirmPassword = payload.get("confirmPassword");

        NguoiDung user = nguoiDungService.getByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu cũ không đúng");
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("Mật khẩu mới không khớp");
        }

        if (newPassword.length() < 3) {
            return ResponseEntity.badRequest().body("Mật khẩu mới phải ít nhất 3 ký tự");
        }

        // Mã hóa mật khẩu mới và lưu
        user.setPassword(passwordEncoder.encode(newPassword));
        nguoiDungService.mergeUser(user);

        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PostMapping("/secure/chuyen-vai-tro-nguoi-ban")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> chuyenVaiTroNguoiBan(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập.");
        }

        NguoiDung nguoiDung = nguoiDungService.getByUsername(principal.getName());

        if (nguoiDung == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng.");
        }

        if ("ROLE_NGUOIBAN".equals(nguoiDung.getVaiTro())) {
            return ResponseEntity.badRequest().body("Bạn đã là người bán.");
        }

        nguoiDung.setVaiTro("ROLE_NGUOIBAN");
        nguoiDungService.mergeUser(nguoiDung);

        return ResponseEntity.ok("Cập nhật vai trò sang người bán thành công.");
    }
}
