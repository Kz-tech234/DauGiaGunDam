/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.pojo.LoaiMoHinh;
import com.ndk.repositories.LoaiMoHinhRepository;
import com.ndk.services.LoaiMoHinhService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
public class LoaiMoHinhServiceImpl implements LoaiMoHinhService{
    @Autowired
    private LoaiMoHinhRepository loaiSanPhamRepository;

    @Override
    public LoaiMoHinh addLoaiSanPham(LoaiMoHinh loaiSanPham) {
        return loaiSanPhamRepository.add(loaiSanPham);
    }
    
    @Override
    public List<LoaiMoHinh> getLoaiSanPham() {
        return loaiSanPhamRepository.getLoaiSanPham();
    }
    
    @Override
    public List<LoaiMoHinh> getLoaiSanPhamHoatDong() {
        return loaiSanPhamRepository.getLoaiSanPhamHoatDong();
    }
    
    @Override
    public boolean deleteLoaiSanPham(int id) {
        return loaiSanPhamRepository.deleteLoaiSanPham(id);
    }

    @Override
    public boolean khoaLoaiSanPham(int id) {
        return loaiSanPhamRepository.khoaLoaiSanPham(id);
    }

    @Override
    public boolean moKhoaLoaiSanPham(int id) {
        return loaiSanPhamRepository.moKhoaLoaiSanPham(id);
    }
}
