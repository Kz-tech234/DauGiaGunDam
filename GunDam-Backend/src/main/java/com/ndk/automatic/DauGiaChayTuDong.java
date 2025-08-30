/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.automatic;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.DonThangDauGia;
import com.ndk.services.CuocDauGiaService;
import com.ndk.services.DonThangDauGiaService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Component
public class DauGiaChayTuDong {
    @Autowired
    private CuocDauGiaService phienDauGiaService;

    @Autowired
    private DonThangDauGiaService donThanhToanDauGiaService;

    // Chạy mỗi phút
    @Scheduled(fixedRate = 60000)
    public void kiemTraPhienHetHan() {

        List<CuocDauGia> tatCaPhien = phienDauGiaService.getLayTatCaPhien();
        for (CuocDauGia phien : tatCaPhien) {
            if ("dang_dien_ra".equals(phien.getTrangThai())) {
                boolean ketThuc = phienDauGiaService.capNhatKetQuaPhien(phien.getId());

                if (ketThuc) {
                    // Gọi lại phiên để lấy thông tin mới cập nhật
                    CuocDauGia phienCapNhat = phienDauGiaService.getLayPhienTheoId(phien.getId());
                    System.out.println("➡️ Đang kiểm tra phiên #" + phien.getId());
                    // Chỉ tạo đơn nếu có winner và giá chốt
                    if (phienCapNhat.getNguoiThangDauGia() != null && phienCapNhat.getGiaChot() != null) {
                        System.out.println("✅ Điều kiện đúng, tạo đơn cho phiên #" + phien.getId());
                        donThanhToanDauGiaService.taoDon(phienCapNhat);
                        System.out.println("✅ Đã tạo đơn thanh toán cho phiên #" + phien.getId());
                    }
                }
            }
        }
    }

    @Scheduled(fixedRate = 70000)
    public void kiemTraDonQuaHan() {

        // Lấy tất cả các đơn chưa thanh toán và chưa hủy
        List<DonThangDauGia> donChuaThanhToan = donThanhToanDauGiaService.DonQuaHanChuaThanhToan();

        // Lặp qua các đơn hàng để kiểm tra
        for (DonThangDauGia don : donChuaThanhToan) {
            Date today = new Date();
            // Kiểm tra nếu đơn hàng đã quá 3 ngày chưa thanh toán
            if (don.getNgayTao().before(new Date(today.getTime() - 3L * 24 * 60 * 60 * 1000))) {
                // Cập nhật trạng thái đơn hàng thành CANCELLED
                don.setTrangThai(DonThangDauGia.TrangThai.CANCELLED);
                don.setGhiChu("Đơn bị hủy vì quá hạn thanh toán 3 ngày.");
                donThanhToanDauGiaService.update(don);
                System.out.println("Đã hủy đơn #" + don.getId() + " vì quá hạn thanh toán.");
            }
        }
    }
}
