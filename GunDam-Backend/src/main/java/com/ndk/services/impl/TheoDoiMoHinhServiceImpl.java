/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.pojo.TheoDoiMoHinh;
import com.ndk.repositories.TheoDoiMoHinhRepository;
import com.ndk.services.TheoDoiMoHinhService;
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
public class TheoDoiMoHinhServiceImpl implements TheoDoiMoHinhService{
    @Autowired
    private TheoDoiMoHinhRepository repo;

    @Override
    public boolean daTheoDoi(int nguoiDungId, int phienDauGiaId) {
        return repo.kiemTraTheoDoi(nguoiDungId, phienDauGiaId);
    }

    @Override
    public void theoDoi(int nguoiDungId, int phienDauGiaId) {
        repo.themTheoDoi(nguoiDungId, phienDauGiaId);
    }

    @Override
    public void boTheoDoi(int nguoiDungId, int phienDauGiaId) {
        repo.xoaTheoDoi(nguoiDungId, phienDauGiaId);
    }

    @Override
    public List<TheoDoiMoHinh> layDanhSachTheoDoi(int nguoiDungId) {
        return repo.layTheoDoiTheoNguoiDung(nguoiDungId);
    }
}
