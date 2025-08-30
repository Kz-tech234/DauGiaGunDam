/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.DonThangDauGia;
import com.ndk.pojo.NguoiDung;
import java.util.List;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface DonThangDauGiaRepository {
    DonThangDauGia findByPhien(CuocDauGia p);
    List<DonThangDauGia> findByNguoiMua(NguoiDung u);
    DonThangDauGia add(DonThangDauGia d);
    DonThangDauGia update(DonThangDauGia d);
    DonThangDauGia getById(Integer id);
    DonThangDauGia taoDon(CuocDauGia p);
    List<DonThangDauGia> DonQuaHanChuaThanhToan();
    void huyDon(int donId, String lyDo);
}
