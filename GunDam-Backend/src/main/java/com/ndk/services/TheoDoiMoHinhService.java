/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services;

import com.ndk.pojo.TheoDoiMoHinh;
import java.util.List;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface TheoDoiMoHinhService {
    boolean daTheoDoi(int nguoiDungId, int phienDauGiaId);
    void theoDoi(int nguoiDungId, int phienDauGiaId);
    void boTheoDoi(int nguoiDungId, int phienDauGiaId);
    List<TheoDoiMoHinh> layDanhSachTheoDoi(int nguoiDungId);
}
