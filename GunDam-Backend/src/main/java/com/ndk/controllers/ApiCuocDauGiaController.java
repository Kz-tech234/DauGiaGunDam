/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.DonThangDauGia;
import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.NguoiDungCuocDauGia;
import com.ndk.services.CuocDauGiaService;
import com.ndk.services.DonThangDauGiaService;
import com.ndk.services.NguoiDungCuocDauGiaService;
import com.ndk.services.NguoiDungService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Nguyen Dang Khoi
 */
@RestController
@RequestMapping("/api/phiendaugia")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiCuocDauGiaController {
    @Autowired
    private CuocDauGiaService phienDauGiaService;

    @Autowired
    private NguoiDungCuocDauGiaService phienDauGiaNguoiDungService;

    @Autowired
    private DonThangDauGiaService donThanhToanDauGiaService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/phiendaugia")
    public List<CuocDauGia> layTatCaPhienDangDienRa() {
        List<CuocDauGia> all = phienDauGiaService.getLayTatCaPhien();
        return all;
//        return all.stream()
//                .filter(p -> "dang_dien_ra".equals(p.getTrangThai()))
//                .collect(Collectors.toList());
    }

    @GetMapping("/phiendaugia/{id}")
    public ResponseEntity<?> layChiTietPhien(@PathVariable(name = "id") int id) {
        // Gọi service để cập nhật trạng thái, người thắng nếu phiên đã kết thúc
        phienDauGiaService.capNhatKetQuaPhien(id);

        // Lấy lại phiên đã cập nhật
        CuocDauGia phien = phienDauGiaService.getLayPhienTheoId(id);
        if (phien != null) {
            // Cập nhật giá hiện tại
            NguoiDungCuocDauGia max = phienDauGiaNguoiDungService.getGiaCaoNhat(id);
            if (max != null) {
                phien.setGiaHienTai(max.getGiaDau());
            } else {
                //phien.setGiaHienTai(phien.getSanPham().getGiaKhoiDiem());
                phien.setGiaHienTai(null);
            }

            // Nếu có WINNER + giá chốt thì tạo đơn
//            if (phien.getGiaChot() != null && phien.getNguoiThangDauGia() != null) {
//                donThanhToanDauGiaService.taoDon(phien);
//            }
            return ResponseEntity.ok(phien);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phiên đấu giá");
    }

    @GetMapping("/baidau")
    public ResponseEntity<?> layDanhSachBaiDauCuaNguoiBan(Principal principal) {
        String username = principal.getName();

        // Lấy người dùng từ service hoặc cơ sở dữ liệu
        NguoiDung nguoiDung = nguoiDungService.getByUsername(username);

        if (nguoiDung == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng.");
        }

        // Lấy các phiên đấu giá theo nguoiDang_id của người dùng hiện tại
        List<CuocDauGia> phienDauGias = phienDauGiaService.getPhienDauByNguoiDangId(nguoiDung.getId());

        if (phienDauGias.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có bài đấu giá nào.");
        }

        for (CuocDauGia phien : phienDauGias) {
            // Lấy giá cao nhất từ các người tham gia đấu giá
            NguoiDungCuocDauGia maxBid = phienDauGiaNguoiDungService.getGiaCaoNhat(phien.getId());
            if (maxBid != null) {
                phien.setGiaHienTai(maxBid.getGiaDau());  // Cập nhật giá hiện tại
            } else {
                phien.setGiaHienTai(null);  // Nếu chưa có ai đấu giá
            }
            DonThangDauGia don = donThanhToanDauGiaService.findByPhien(phien);
            phien.setDonThanhToan(don);
        }

        return ResponseEntity.ok(phienDauGias);
    }

}
