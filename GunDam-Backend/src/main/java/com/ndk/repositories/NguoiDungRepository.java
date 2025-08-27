/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories;

import com.ndk.pojo.NguoiDung;
import java.util.List;

/**
 *
 * @author Nguyen Dang Khoi
 */
public interface NguoiDungRepository {
    NguoiDung getByUsername(String username);
    NguoiDung getById(int id);
    NguoiDung addUser(NguoiDung u);
    boolean khoaUser(int id);
    boolean moKhoaUser(int id);
    NguoiDung merge(NguoiDung u);
    boolean vaiTro(String username, String vaiTro);
    boolean deleteUser(int id);
    boolean authenticate(String username, String rawPassword);
    List<NguoiDung> getAllUsers();
    boolean duyetNguoiDung(int userId);
}
