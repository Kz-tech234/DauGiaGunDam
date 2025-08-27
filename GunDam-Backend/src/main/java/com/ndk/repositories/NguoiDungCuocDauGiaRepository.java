/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories;

import com.ndk.pojo.NguoiDungCuocDauGia;
import java.util.List;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface NguoiDungCuocDauGiaRepository {
    NguoiDungCuocDauGia datGia(NguoiDungCuocDauGia p);
    List<NguoiDungCuocDauGia> getLichSuByPhien(int phienId);
    List<NguoiDungCuocDauGia> getLichSuByNguoiDung(int nguoiDungId);
    NguoiDungCuocDauGia getGiaCaoNhat(int phienId);
    List<NguoiDungCuocDauGia> getByPhien(int phienId);
}
