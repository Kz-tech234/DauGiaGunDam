/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.DonThangDauGia;
import com.ndk.pojo.NguoiDung;
import com.ndk.repositories.DonThangDauGiaRepository;
import com.ndk.services.DonThangDauGiaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
@Transactional
public class DonThangDauGiaServiceImpl implements DonThangDauGiaService{
    @Autowired
    private DonThangDauGiaRepository repo;

    @Override
    public DonThangDauGia findByPhien(CuocDauGia p) {
        return repo.findByPhien(p);
    }

    @Override
    public List<DonThangDauGia> findByNguoiMua(NguoiDung u) {
        return repo.findByNguoiMua(u);
    }

    @Override
    public DonThangDauGia add(DonThangDauGia d) {
        return repo.add(d);
    }

    @Override
    public DonThangDauGia update(DonThangDauGia d) {
        return repo.update(d);
    }

    @Override
    public DonThangDauGia getById(Integer id) {
        return repo.getById(id);
    }

    @Override
    public DonThangDauGia taoDon(CuocDauGia p) {
        return repo.taoDon(p);
    }

    @Override
    public List<DonThangDauGia> DonQuaHanChuaThanhToan() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.DATE, -3); // Trừ 3 ngày
        java.util.Date deadline = calendar.getTime();

        return repo.DonQuaHanChuaThanhToan(DonThangDauGia.TrangThai.PENDING, deadline);
    }

    @Override
    public void huyDon(int donId, String lyDo) {
        repo.huyDon(donId, lyDo);
    }
}
