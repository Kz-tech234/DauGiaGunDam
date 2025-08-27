/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services;

import com.ndk.pojo.CuocDauGia;
import java.util.List;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface CuocDauGiaService {
    CuocDauGia themPhienDauGia(CuocDauGia p);
    List<CuocDauGia> getLayTatCaPhien();
    CuocDauGia getLayPhienTheoId(int id);
    boolean duyetPhien(int id); // admin duyệt
    boolean capNhatKetQuaPhien(int phienId);
    CuocDauGia capNhatPhien(CuocDauGia p);
    List<CuocDauGia> getPhienDauByNguoiDangId(int nguoiDangId);
}
