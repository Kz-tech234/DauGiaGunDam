/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.TaiKhoanNganHang;
import com.ndk.repositories.TaiKhoanNganHangRepository;
import com.ndk.services.TaiKhoanNganHangService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
public class TaiKhoanNganHangServiceImpl implements TaiKhoanNganHangService{
    @Autowired
    private TaiKhoanNganHangRepository repo;

    @Override
    public List<TaiKhoanNganHang> findByNguoiBan(NguoiDung nd) {
        return repo.findByNguoiBan(nd);
    }

    @Override
    public TaiKhoanNganHang addTaiKhoan(NguoiDung u, String tenNguoiNhan, String nganHang, String soTaiKhoan, MultipartFile qrFile) {
        return repo.addTaiKhoan(u, tenNguoiNhan, nganHang, soTaiKhoan, qrFile);
    }
    
    @Override
    public boolean taiKhoanNguoiBan(NguoiDung nd) {
        return !repo.findByNguoiBan(nd).isEmpty();
    }
}
