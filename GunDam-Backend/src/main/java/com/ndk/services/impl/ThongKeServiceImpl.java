/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.ndk.dto.ThongKeDTO;
import com.ndk.repositories.ThongKeRepository;
import com.ndk.services.ThongKeService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
public class ThongKeServiceImpl implements ThongKeService{
    @Autowired
    private ThongKeRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<ThongKeDTO> thongKeTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
        return repo.thongKeTheoNgay(tuNgay, denNgay);
    }
}
