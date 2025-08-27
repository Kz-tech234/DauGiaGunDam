/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services;

import com.ndk.dto.ThongKeDTO;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface ThongKeService {
    List<ThongKeDTO> thongKeTheoNgay(LocalDate tuNgay, LocalDate denNgay);
}
