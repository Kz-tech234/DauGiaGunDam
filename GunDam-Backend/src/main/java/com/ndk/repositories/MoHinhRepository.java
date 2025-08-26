/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories;

import com.ndk.pojo.MoHinh;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Tran Quoc Phong
 */
public interface MoHinhRepository {
    MoHinh addSanPham(String tenSanPham, String moTa, BigDecimal giaKhoiDiem, BigDecimal buocNhay, Date thoiGianKetThuc, int loaiSanPhamId, String username, MultipartFile avatar);
    List<MoHinh> getAll();
    boolean updateTrangThai(int id, String trangThai);
    MoHinh getById(int id);
    boolean delete(int id);
    List<MoHinh> getSanPhamsTheoTrangThai(String trangThai);
}
