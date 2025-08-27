/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.MoHinh;
import com.ndk.pojo.NguoiDung;
import com.ndk.services.MoHinhService;
import com.ndk.services.NguoiDungService;
import com.ndk.services.TaiKhoanNganHangService;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/sanpham")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiMoHinhController {
    @Autowired
    private MoHinhService sanPhamService;

    @Autowired
    private NguoiDungService nguoiDungService;
    
    @Autowired
    private TaiKhoanNganHangService thongTinTaiKhoanService;

    // API để đăng sản phẩm mới
    @PostMapping("/dangsanpham")
    public ResponseEntity<?> createProduct(@RequestParam("tenSanPham") String tenSanPham,
                                           @RequestParam("moTa") String moTa,
                                           @RequestParam("giaKhoiDiem") BigDecimal giaKhoiDiem,
                                           @RequestParam("buocNhay") BigDecimal buocNhay,
                                           @RequestParam("thoiGianKetThuc") String thoiGianKetThucStr,
                                           @RequestParam("loaiSanPham_id") int loaiSanPhamId,
                                           @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                           Principal principal) {
        
        // Lấy thông tin người dùng đang đăng nhập
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập để thực hiện thao tác này");
        }
        String username = principal.getName();

        // Kiểm tra nếu người dùng không có vai trò "ROLE_NGUOIBAN"
        if (!nguoiDungService.vaiTro(username, "ROLE_NGUOIBAN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Chỉ người bán mới có quyền đăng sản phẩm.");
        }
        
        // Kiểm tra nếu người bán chưa có thông tin tài khoản ngân hàng
        NguoiDung nguoiBan = nguoiDungService.getByUsername(username);
        if (!thongTinTaiKhoanService.taiKhoanNguoiBan(nguoiBan)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bạn chưa thêm thông tin tài khoản ngân hàng. Vui lòng vào mục -Thêm tk ngân hàng- phía trên để điền thông tin.");
        }

        try {
            // Parse thời gian kết thúc từ String -> Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date thoiGianKetThuc = sdf.parse(thoiGianKetThucStr);
            
            // Gọi service để thêm sản phẩm
            MoHinh sanPham = sanPhamService.addSanPham(
                tenSanPham, moTa, giaKhoiDiem, buocNhay, thoiGianKetThuc, loaiSanPhamId, username, avatar
            );
            return new ResponseEntity<>(sanPham, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi đăng sản phẩm: " + e.getMessage());
        }
    }
}
