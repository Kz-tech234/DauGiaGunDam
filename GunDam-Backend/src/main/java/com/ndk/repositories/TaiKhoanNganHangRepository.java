/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories;

import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.TaiKhoanNganHang;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Tran Quoc Phong
 */
public interface TaiKhoanNganHangRepository {
    List<TaiKhoanNganHang> findByNguoiBan(NguoiDung nd);
    TaiKhoanNganHang addTaiKhoan(NguoiDung u, String tenNguoiNhan, String nganHang, String soTaiKhoan, MultipartFile qrFile);
}
