/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services;

import com.ndk.pojo.MoHinh;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface MoHinhService {
    MoHinh addSanPham(String tenSanPham, String moTa, BigDecimal giaKhoiDiem, BigDecimal buocNhay, Date thoiGianKetThuc, int loaiSanPhamId, String username, MultipartFile avatar);
    List<MoHinh> getAllSanPham();
    boolean updateTrangThai(int id, String trangThai);
    MoHinh getSanPhamById(int id);
    boolean deleteSanPham(int id);
    List<MoHinh> getSanPhamsTheoTrangThai(String trangThai);
}
