/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.services.LoaiMoHinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Nguyen Dang Khoi
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiLoaiMoHinhController {
    @Autowired
    private LoaiMoHinhService loaiSanPhamService;

    @GetMapping("/loaisanpham")
    public ResponseEntity<?> getAllLoaiSanPham() {
        return ResponseEntity.ok(loaiSanPhamService.getLoaiSanPhamHoatDong());
    }
}
