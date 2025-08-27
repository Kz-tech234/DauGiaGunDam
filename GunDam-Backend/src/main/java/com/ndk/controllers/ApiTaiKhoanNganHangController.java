/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.TaiKhoanNganHang;
import com.ndk.services.CuocDauGiaService;
import com.ndk.services.NguoiDungService;
import com.ndk.services.TaiKhoanNganHangService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
@RestController
@RequestMapping("/api/taikhoannganhang")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiTaiKhoanNganHangController {
    @Autowired
    private TaiKhoanNganHangService tkService;

    @Autowired
    private NguoiDungService userService;
    
    @Autowired
    private CuocDauGiaService phienService;

    // Lấy tất cả TK của người bán đang đăng nhập
    @GetMapping("/cua-toi")
    public ResponseEntity<?> taiKhoanCuaToi(Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).build();

        NguoiDung u = userService.getByUsername(principal.getName());
        return ResponseEntity.ok(tkService.findByNguoiBan(u));
    }

    // Thêm tài khoản mới
    @PostMapping("/them")
    public ResponseEntity<?> themTaiKhoan(
    @RequestParam("tenNguoiNhan") String tenNguoiNhan,
    @RequestParam("nganHang") String nganHang,
    @RequestParam("soTaiKhoan") String soTaiKhoan,
    @RequestParam(value = "qrFile", required = false) MultipartFile qrFile,
    Principal principal) {


    if (principal == null)
    return ResponseEntity.status(401).build();


    NguoiDung u = userService.getByUsername(principal.getName());


    TaiKhoanNganHang tk = tkService.addTaiKhoan(u, tenNguoiNhan, nganHang, soTaiKhoan, qrFile);


    return ResponseEntity.ok(tk);
    }
    
    @GetMapping("/nguoiban/{phienId}")
    public ResponseEntity<?> layTaiKhoanNguoiBan(@PathVariable("phienId") int phienId) {   
        try {
            CuocDauGia phien = phienService.getLayPhienTheoId(phienId);
            if (phien == null || phien.getNguoiDang() == null)
                return ResponseEntity.notFound().build();

            List<TaiKhoanNganHang> ds = tkService.findByNguoiBan(phien.getNguoiDang());

            if (ds.isEmpty()) return ResponseEntity.ok().body(null);
            return ResponseEntity.ok(ds.get(0));

        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(500).body("Lỗi khi lấy tài khoản người bán.");
        }
    }
}
