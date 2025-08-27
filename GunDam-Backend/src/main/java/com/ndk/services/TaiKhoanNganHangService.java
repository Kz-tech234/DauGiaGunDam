/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services;

import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.TaiKhoanNganHang;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface TaiKhoanNganHangService {
    List<TaiKhoanNganHang> findByNguoiBan(NguoiDung nd);
    TaiKhoanNganHang addTaiKhoan(NguoiDung u, String tenNguoiNhan, String nganHang, String soTaiKhoan, MultipartFile qrFile);
    boolean taiKhoanNguoiBan(NguoiDung nd);
}
