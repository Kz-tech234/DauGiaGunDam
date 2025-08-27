/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.pojo.MoHinh;
import com.ndk.repositories.MoHinhRepository;
import com.ndk.services.MoHinhService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
@Transactional
public class MoHinhServiceImpl implements MoHinhService{
    @Autowired
    private MoHinhRepository sanPhamRepository;

    @Override
    public MoHinh addSanPham(String tenSanPham, String moTa, BigDecimal giaKhoiDiem, BigDecimal buocNhay, Date thoiGianKetThuc, int loaiSanPhamId, String username, MultipartFile avatar) {
        return sanPhamRepository.addSanPham(tenSanPham, moTa, giaKhoiDiem, buocNhay, thoiGianKetThuc, loaiSanPhamId, username, avatar);
    }

    @Override
    public List<MoHinh> getAllSanPham() {
        return sanPhamRepository.getAll();
    }
    
    @Override
    public boolean updateTrangThai(int id, String trangThai) {
        return sanPhamRepository.updateTrangThai(id, trangThai);
    }

    @Override
    public MoHinh getSanPhamById(int id) {
        return sanPhamRepository.getById(id);
    }

    @Override
    public boolean deleteSanPham(int id) {
        return sanPhamRepository.delete(id);
    }
    
    @Override
    public List<MoHinh> getSanPhamsTheoTrangThai(String trangThai) {
        return sanPhamRepository.getSanPhamsTheoTrangThai(trangThai);
    }
}
