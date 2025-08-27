/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.pojo.CuocDauGia;
import com.ndk.repositories.CuocDauGiaRepository;
import com.ndk.services.CuocDauGiaService;
import com.ndk.services.MailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
public class CuocDauGiaServiceImpl implements CuocDauGiaService{
    @Autowired
    private CuocDauGiaRepository phienDauGiaRepository;

    @Autowired
    private MailService mailService;

    @Override
    public CuocDauGia themPhienDauGia(CuocDauGia p) {
        // Mặc định trạng thái là "CHO_DUYET"
        //p.setTrangThai("CHO_DUYET");
        return phienDauGiaRepository.themPhienDauGia(p);
    }

    @Override
    public List<CuocDauGia> getLayTatCaPhien() {
        return phienDauGiaRepository.getLayTatCaPhien();
    }

    @Override
    public CuocDauGia getLayPhienTheoId(int id) {
        return phienDauGiaRepository.getLayPhienTheoId(id);
    }

    @Override
    public boolean duyetPhien(int id) {
        return phienDauGiaRepository.capNhatTrangThai(id, "DUOC_DUYET");
    }

    @Override
    public CuocDauGia capNhatPhien(CuocDauGia p) {
        return phienDauGiaRepository.capNhatPhien(p);
    }

    @Override
    @Transactional
    public boolean capNhatKetQuaPhien(int phienId) {
        phienDauGiaRepository.capNhatKetQuaPhien(phienId);

        CuocDauGia phien = phienDauGiaRepository.getLayPhienTheoId(phienId);
        if (phien == null) {
            return false;
        }

        boolean ketThuc = "da_ket_thuc".equalsIgnoreCase(phien.getTrangThai());
        boolean chuaThongBao = (phien.getDaThongBaoKQ() == null) || !phien.getDaThongBaoKQ();
        boolean coWinner = phien.getNguoiThangDauGia() != null && phien.getGiaChot() != null;

        if (ketThuc && coWinner && chuaThongBao) {
            String email = phien.getNguoiThangDauGia().getEmail();
            if (email != null && !email.isBlank()) {
                try {
                    mailService.sendWinnerEmail(
                            email,
                            phien.getNguoiThangDauGia().getHoTen(),
                            phien.getSanPham().getTenSanPham(),
                            phien.getGiaChot()
                    );
                    // đánh dấu đã thông báo để không gửi lặp
                    phien.setDaThongBaoKQ(true);
                    // vì đang @Transactional và entity managed, flush sẽ tự update
                    // nếu bạn muốn rõ ràng, thêm repo.update(phien);
                } catch (Exception ex) {
                    ex.printStackTrace(); // log, không rollback giao dịch
                }
            }
        }
        return ketThuc;
    }
    
    @Override
    public List<CuocDauGia> getPhienDauByNguoiDangId(int nguoiDangId) {
        return phienDauGiaRepository.getPhienDauByNguoiDangId(nguoiDangId);
    }
}
