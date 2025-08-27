/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.pojo.LoaiMoHinh;
import com.ndk.services.LoaiMoHinhService;
import com.ndk.services.NguoiDungService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Controller
public class LoaiMoHinhController {
    @Autowired
    private LoaiMoHinhService loaiSanPhamService;
    
    @Autowired
    private NguoiDungService nguoiDungService;

    // Hiển thị form
    @GetMapping("/admin/loaiSanPham")
    public String loaiSanPhamForm(Model model) {
        model.addAttribute("loaiSanPham", new LoaiMoHinh());
        model.addAttribute("dsLoai", loaiSanPhamService.getLoaiSanPham());
        return "loaiSanPham";
    }

    // Xử lý form
    @PostMapping("/admin/loaiSanPham")
    public String themLoaiSanPham(@ModelAttribute @Valid LoaiMoHinh loaiSanPham,
                                   BindingResult result,
                                   Model model,
                                   Principal principal) {
        if (!result.hasErrors()) {
            String username = principal.getName();
            loaiSanPham.setNguoiDung(nguoiDungService.getByUsername(username));
            loaiSanPham.setTrangThai("HOAT_DONG");
            
            loaiSanPhamService.addLoaiSanPham(loaiSanPham);
            model.addAttribute("successMessage", "Thêm loại sản phẩm thành công!");
        }
        model.addAttribute("dsLoai", loaiSanPhamService.getLoaiSanPham());
        return "loaiSanPham";
    }
    
    @PostMapping("/admin/xoa-loai")
    public String xoaLoai(@RequestParam("id") int id, Model model) {
        try {
            boolean result = loaiSanPhamService.deleteLoaiSanPham(id);
            if (result) {
                model.addAttribute("successMessage", "Xóa loại sản phẩm thành công!");
            } else {
                model.addAttribute("successMessage", "Không thể xóa và bạn phải khóa loại sản phẩm!");
            }
        } catch (Exception e) {
            model.addAttribute("successMessage", "Không thể xóa và bạn phải khóa loại sản phẩm!");
        }

        model.addAttribute("loaiSanPham", new LoaiMoHinh());
        model.addAttribute("dsLoai", loaiSanPhamService.getLoaiSanPham());
        return "loaiSanPham";
    }

    @PostMapping("/admin/khoa-loai")
    public String khoaLoai(@RequestParam("id") int id, Model model) {
        loaiSanPhamService.khoaLoaiSanPham(id);
        model.addAttribute("successMessage", "Đã khóa loại sản phẩm!");
        model.addAttribute("loaiSanPham", new LoaiMoHinh());
        model.addAttribute("dsLoai", loaiSanPhamService.getLoaiSanPham());
        return "loaiSanPham";
    }

    @PostMapping("/admin/mo-khoa-loai")
    public String moKhoaLoai(@RequestParam("id") int id, Model model) {
        loaiSanPhamService.moKhoaLoaiSanPham(id);
        model.addAttribute("successMessage", "Đã mở khóa loại sản phẩm!");
        model.addAttribute("loaiSanPham", new LoaiMoHinh());
        model.addAttribute("dsLoai", loaiSanPhamService.getLoaiSanPham());
        return "loaiSanPham";
    }
}
