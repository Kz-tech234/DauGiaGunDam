/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.NguoiDung;
import com.ndk.services.NguoiDungService;
import com.ndk.services.TheoDoiMoHinhService;
import java.security.Principal;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Nguyen Dang Khoi
 */
@RestController
@RequestMapping("/api/theodoisanpham")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiTheoDoiMoHinhController {
    @Autowired
    private TheoDoiMoHinhService theoDoiService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/{phienId}")
    public ResponseEntity<?> checkTheoDoi(@PathVariable int phienId, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");

        String username = principal.getName();
        NguoiDung nguoiDung = nguoiDungService.getByUsername(username);
        if (nguoiDung == null)
            return ResponseEntity.status(404).body("Không tìm thấy người dùng");

        boolean daTheoDoi = theoDoiService.daTheoDoi(nguoiDung.getId(), phienId);
        return ResponseEntity.ok(Collections.singletonMap("daTheoDoi", daTheoDoi));
    }
    
    @GetMapping("/danhsach")
    public ResponseEntity<?> danhSachDangTheoDoi(Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");

        String username = principal.getName();
        NguoiDung nguoiDung = nguoiDungService.getByUsername(username);
        if (nguoiDung == null)
            return ResponseEntity.status(404).body("Không tìm thấy người dùng");

        return ResponseEntity.ok(theoDoiService.layDanhSachTheoDoi(nguoiDung.getId()));
    }

    @PostMapping("/them/{phienId}")
    public ResponseEntity<?> theoDoi(@PathVariable("phienId") int phienId, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");

        String username = principal.getName();
        NguoiDung nguoiDung = nguoiDungService.getByUsername(username);
        if (nguoiDung == null)
            return ResponseEntity.status(404).body("Không tìm thấy người dùng");

        theoDoiService.theoDoi(nguoiDung.getId(), phienId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/xoa/{phienId}")
    public ResponseEntity<?> boTheoDoi(@PathVariable("phienId") int phienId, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");

        String username = principal.getName();
        NguoiDung nguoiDung = nguoiDungService.getByUsername(username);
        if (nguoiDung == null)
            return ResponseEntity.status(404).body("Không tìm thấy người dùng");

        theoDoiService.boTheoDoi(nguoiDung.getId(), phienId);
        return ResponseEntity.ok().build();
    }
}
