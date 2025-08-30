/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.MoHinh;
import com.ndk.services.CuocDauGiaService;
import com.ndk.services.MoHinhService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Controller
@RequestMapping("/admin")
public class CuocDauGiaController {
    @Autowired
    private MoHinhService sanPhamService;

    @Autowired
    private CuocDauGiaService phienDauGiaService;

    @GetMapping("/duyetSanPham")
    public String duyetSanPhamView(Model model) {
        List<MoHinh> dsChoDuyet = sanPhamService.getSanPhamsTheoTrangThai("CHO_DUYET");
        model.addAttribute("sanPhamsChoDuyet", dsChoDuyet);
        return "duyetBaiMoHinh";
    }

    @PostMapping("/duyet-san-pham")
    public String duyetSanPham(@RequestParam("id") int id, @RequestParam("action") String action) {
        MoHinh sp = sanPhamService.getSanPhamById(id);
        if (sp != null) {
            if ("duyet".equals(action)) {
                // cập nhật trạng thái
                sanPhamService.updateTrangThai(id, "DUYET");

                // tạo phiên đấu giá
                CuocDauGia phien = new CuocDauGia();
                phien.setNguoiDang(sp.getNguoiDung());
                phien.setSanPham(sp);
                phien.setTrangThai("dang_dien_ra");
                phien.setThoiGianBatDau(sp.getNgayDang());
                phien.setThoiGianKetThuc(sp.getThoiGianKetThuc());
                phien.setDaThongBaoKQ(Boolean.FALSE);
                phienDauGiaService.themPhienDauGia(phien);

            } else if ("khong_duyet".equals(action)) {
                sanPhamService.updateTrangThai(id, "KHONG_DUYET");
            }
        }
        return "redirect:/admin/duyetSanPham";
    }
}
