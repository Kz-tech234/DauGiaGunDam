/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services;

import com.ndk.pojo.LoaiMoHinh;
import java.util.List;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface LoaiMoHinhService {
    LoaiMoHinh addLoaiSanPham(LoaiMoHinh loaiSanPham);
    List<LoaiMoHinh> getLoaiSanPham();
    List<LoaiMoHinh> getLoaiSanPhamHoatDong();
    boolean deleteLoaiSanPham(int id);
    boolean khoaLoaiSanPham(int id);
    boolean moKhoaLoaiSanPham(int id);
}
