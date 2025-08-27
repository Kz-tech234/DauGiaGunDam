/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.DonThangDauGia;
import com.ndk.pojo.NguoiDung;
import com.ndk.services.CuocDauGiaService;
import com.ndk.services.DonThangDauGiaService;
import com.ndk.services.NguoiDungService;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Nguyen Dang Khoi
 */
@RestController
@RequestMapping("/api/thanhtoan")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiDonThangDauGiaController {
    @Autowired
    private DonThangDauGiaService donService;
    @Autowired
    private CuocDauGiaService phienService;
    @Autowired
    private NguoiDungService userService;

    @GetMapping("/cua-toi")
    public ResponseEntity<?> donCuaToi(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        NguoiDung u = userService.getByUsername(principal.getName());
        return ResponseEntity.ok(donService.findByNguoiMua(u));
    }

    // Cập nhật thanh toán (COD/BANK + địa chỉ…)
    @PostMapping("/{donId}/thanh-toan")
    public ResponseEntity<?> thanhToan(@PathVariable("donId") int donId,
            @RequestBody Map<String, String> body,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        DonThangDauGia don = donService.getById(donId);
        if (don == null) {
            return ResponseEntity.notFound().build();
        }

        // Chỉ chủ đơn (người thắng) mới được thao tác
        NguoiDung nguoiDangNhap = userService.getByUsername(principal.getName());
        if (nguoiDangNhap == null || !nguoiDangNhap.getId().equals(don.getNguoiMua().getId())) {
            return ResponseEntity.status(403).body("Bạn không có quyền thao tác đơn này");
        }

        if (don.getTrangThai() == DonThangDauGia.TrangThai.SELLER_REVIEW) {
            return ResponseEntity.badRequest().body("Đơn đã thanh toán");
        }

        try {
            // Lấy dữ liệu
            String phuongThuc = body.getOrDefault("phuongThuc", "COD");
            String hoTenNhan = body.get("hoTenNhan");
            String soDienThoai = body.get("soDienThoai");
            String diaChiNhan = body.get("diaChiNhan");
            String ghiChu = body.get("ghiChu");

            // Validate đơn giản
            if (hoTenNhan == null || hoTenNhan.isBlank()
                    || soDienThoai == null || soDienThoai.isBlank()
                    || diaChiNhan == null || diaChiNhan.isBlank()) {
                return ResponseEntity.badRequest().body("Vui lòng nhập đủ họ tên, số điện thoại và địa chỉ nhận");
            }

            // Set thông tin thanh toán
            DonThangDauGia.PhuongThuc pm
                    = "BANK".equalsIgnoreCase(phuongThuc) ? DonThangDauGia.PhuongThuc.BANK
                    : DonThangDauGia.PhuongThuc.COD;
            don.setPhuongThuc(pm);
            don.setHoTenNhan(hoTenNhan);
            don.setSoDienThoai(soDienThoai);
            don.setDiaChiNhan(diaChiNhan);
            don.setGhiChu(ghiChu);
            don.setTrangThai(DonThangDauGia.TrangThai.SELLER_REVIEW);
            don.setNgayThanhToan(new java.util.Date());

            return ResponseEntity.ok(donService.update(don));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Lỗi xử lý thanh toán");
        }
    }

    @PutMapping("/{donId}/xac-nhan")
    public ResponseEntity<?> xacNhanDon(@PathVariable("donId") int donId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        DonThangDauGia don = donService.getById(donId);
        if (don == null) {
            return ResponseEntity.notFound().build();
        }

        // Lấy người dùng hiện tại
        NguoiDung nguoiDangNhap = userService.getByUsername(principal.getName());

        // Chỉ cho phép người đăng phiên đấu xác nhận đơn
        CuocDauGia phien = don.getPhienDauGia();
        if (phien == null || !phien.getNguoiDang().getId().equals(nguoiDangNhap.getId())) {
            return ResponseEntity.status(403).body("Bạn không có quyền xác nhận đơn này");
        }

        // Chỉ xác nhận nếu trạng thái hiện tại là SELLER_REVIEW
        if (don.getTrangThai() != DonThangDauGia.TrangThai.SELLER_REVIEW) {
            return ResponseEntity.badRequest().body("Chỉ được xác nhận đơn ở trạng thái SELLER_REVIEW");
        }

        // Cập nhật trạng thái sang PAID
        don.setTrangThai(DonThangDauGia.TrangThai.PAID);
        don.setNgaySellerDuyet(new Date());

        return ResponseEntity.ok(donService.update(don));
    }

    @PutMapping("/huy/{id}")
    public ResponseEntity<?> huyDon(@PathVariable("id") int id, @RequestBody Map<String, String> body) {
        String lyDo = body.get("lyDo");

        if (lyDo == null || lyDo.isBlank()) {
            return ResponseEntity.badRequest().body("Lý do không được để trống.");
        }

        donService.huyDon(id, lyDo);
        return ResponseEntity.ok("Đơn đã được hủy.");
    }
}
