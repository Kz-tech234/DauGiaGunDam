/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories;

import com.ndk.pojo.TheoDoiMoHinh;
import java.util.List;

/**
 *
 * @author Tran Quoc Phong
 */
public interface TheoDoiMoHinhRepository {
    boolean kiemTraTheoDoi(int nguoiDungId, int phienDauGiaId);
    void themTheoDoi(int nguoiDungId, int phienDauGiaId);
    void xoaTheoDoi(int nguoiDungId, int phienDauGiaId);
    List<TheoDoiMoHinh> layTheoDoiTheoNguoiDung(int nguoiDungId);
}
