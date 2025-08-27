/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.pojo.NguoiDungCuocDauGia;
import com.ndk.repositories.NguoiDungCuocDauGiaRepository;
import com.ndk.services.NguoiDungCuocDauGiaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
public class NguoiDungCuocDauGiaServiceImpl implements NguoiDungCuocDauGiaService{
    @Autowired
    private NguoiDungCuocDauGiaRepository phienDauGiaNguoiDungRepository;

    @Override
    public NguoiDungCuocDauGia datGia(NguoiDungCuocDauGia p) {
        return phienDauGiaNguoiDungRepository.datGia(p);
    }

    @Override
    public List<NguoiDungCuocDauGia> getLichSuByPhien(int phienId) {
        return phienDauGiaNguoiDungRepository.getLichSuByPhien(phienId);
    }

    @Override
    public List<NguoiDungCuocDauGia> getLichSuByNguoiDung(int nguoiDungId) {
        return phienDauGiaNguoiDungRepository.getLichSuByNguoiDung(nguoiDungId);
    }

    @Override
    public NguoiDungCuocDauGia getGiaCaoNhat(int phienId) {
        return phienDauGiaNguoiDungRepository.getGiaCaoNhat(phienId);
    }
    
    @Override
    public List<NguoiDungCuocDauGia> getByPhien(int phienId) {
        return phienDauGiaNguoiDungRepository.getByPhien(phienId);
    }
}
