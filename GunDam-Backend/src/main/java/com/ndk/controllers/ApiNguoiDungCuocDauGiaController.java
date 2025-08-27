/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.dto.DauDTO;
import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.NguoiDungCuocDauGia;
import com.ndk.services.CuocDauGiaService;
import com.ndk.services.NguoiDungCuocDauGiaService;
import com.ndk.services.NguoiDungService;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Nguyen Dang Khoi
 */
@RestController
@RequestMapping("/api/phiendaugianguoidung")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiNguoiDungCuocDauGiaController {
    @Autowired
    private NguoiDungCuocDauGiaService phienDauGiaNguoiDungService;

    @Autowired
    private CuocDauGiaService phienDauGiaService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @PostMapping("/dat-gia")
    public ResponseEntity<?> datGia(@RequestBody Map<String, String> params, Principal principal) {
        try {
            // Kiểm tra đăng nhập
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập để đấu giá");
            }
            String username = principal.getName();
            NguoiDung nguoiDung = this.nguoiDungService.getByUsername(username);
            if (nguoiDung == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy thông tin người dùng");
            }

            // Lấy thông tin phiên và giá
            int phienId = Integer.parseInt(params.get("phienDauGiaId"));
            BigDecimal giaMoi = new BigDecimal(params.get("gia"));

            CuocDauGia phien = this.phienDauGiaService.getLayPhienTheoId(phienId);
            if (phien == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy phiên đấu giá");
            }

            // Không cho người bán đấu giá sản phẩm của mình
            if (phien.getSanPham() != null
                    && phien.getSanPham().getNguoiDung() != null
                    && phien.getSanPham().getNguoiDung().getId().equals(nguoiDung.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bạn không thể đấu giá sản phẩm của chính mình");
            }

            // Lấy giá cao nhất hiện tại
            NguoiDungCuocDauGia giaCaoNhatRecord = this.phienDauGiaNguoiDungService.getGiaCaoNhat(phienId);
            BigDecimal giaKhoiDiem = phien.getSanPham().getGiaKhoiDiem();
            BigDecimal buocNhay = phien.getSanPham().getBuocNhay();

            boolean laBidDauTien = (giaCaoNhatRecord == null);
            BigDecimal giaToiThieu = laBidDauTien
                    ? giaKhoiDiem
                    : giaCaoNhatRecord.getGiaDau().add(buocNhay);

            if (giaMoi.compareTo(giaToiThieu) < 0) {
                return ResponseEntity.badRequest()
                        .body("Giá bạn nhập phải lớn hơn hoặc bằng " + giaToiThieu.toPlainString());
            }

            // Kiểm tra bước nhảy
            BigDecimal mocSoSanh = laBidDauTien ? giaKhoiDiem : giaCaoNhatRecord.getGiaDau();
            BigDecimal du = giaMoi.subtract(mocSoSanh).remainder(buocNhay);
            if (du.compareTo(BigDecimal.ZERO) != 0) {
                return ResponseEntity.badRequest()
                        .body("Giá phải theo bước nhảy " + buocNhay.toPlainString());
            }

            // Lưu giá đấu
            NguoiDungCuocDauGia pdgNd = new NguoiDungCuocDauGia();
            pdgNd.setPhienDauGia(phien);
            pdgNd.setNguoiDung(nguoiDung);
            pdgNd.setGiaDau(giaMoi);
            pdgNd.setThoiGianDauGia(LocalDateTime.now());

            this.phienDauGiaNguoiDungService.datGia(pdgNd);

            // Cập nhật giaHienTai của phiên 
            phien.setGiaHienTai(giaMoi); // kể cả khi = giá khởi điểm
            this.phienDauGiaService.capNhatPhien(phien);

            return ResponseEntity.ok("Đặt giá thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi xử lý đặt giá: " + e.getMessage());
        }
    }
    
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/lich-su/{phienId}")
    public ResponseEntity<?> lichSu(@PathVariable("phienId") int phienId) {
        var list = phienDauGiaNguoiDungService.getByPhien(phienId);
        if (list == null) return ResponseEntity.ok(List.of());

        // tìm giá cao nhất để đánh dấu
        var max = list.stream().map(NguoiDungCuocDauGia::getGiaDau)
                      .max(BigDecimal::compareTo).orElse(null);

        var dto = list.stream().map(b -> new DauDTO(
                b.getNguoiDung().getHoTen(),
                b.getNguoiDung().getUsername(),
                b.getNguoiDung().getAvatar(),
                b.getGiaDau(),
                b.getThoiGianDauGia(),
                max != null && b.getGiaDau().compareTo(max) == 0
        )).toList();

        return ResponseEntity.ok(dto);
    }
}
