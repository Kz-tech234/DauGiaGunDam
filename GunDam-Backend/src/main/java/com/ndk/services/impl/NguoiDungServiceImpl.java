/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndk.pojo.NguoiDung;
import com.ndk.repositories.NguoiDungRepository;
import com.ndk.services.NguoiDungService;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Service
@Primary
@Transactional
public class NguoiDungServiceImpl implements NguoiDungService, UserDetailsService{
    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    @Autowired
    private BCryptPasswordEncoder passEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public NguoiDung getByUsername(String username) {
        return nguoiDungRepo.getByUsername(username);
    }

    @Override
    public NguoiDung getById(int id) {
        return nguoiDungRepo.getById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">>>> GỌI ĐẾN loadUserByUsername với username: " + username);
        NguoiDung u = nguoiDungRepo.getByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Không tìm thấy user");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        System.out.println("USERNAME: " + u.getUsername());
        System.out.println("ROLE: [" + u.getVaiTro() + "]");
        System.out.println("Generated hash for 123456: " + passEncoder.encode("123456"));
        authorities.add(new SimpleGrantedAuthority(u.getVaiTro()));
        System.out.println("Authorities: " + authorities);

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public NguoiDung addUser(NguoiDung user) {
        user.setPassword(passEncoder.encode(user.getPassword())); // mã hóa
        return nguoiDungRepo.addUser(user);
    }

    @Override
    public boolean khoaUser(int id) {
        return nguoiDungRepo.khoaUser(id);
    }
    
    @Override
    public boolean moKhoaUser(int id) {
        return nguoiDungRepo.moKhoaUser(id);
    }

    @Override
    public NguoiDung mergeUser(NguoiDung user) {
        // Nếu đã có user (tức là đã có ID), gọi merge để cập nhật
        return nguoiDungRepo.merge(user);  // Dùng merge để cập nhật thay vì persist
    }

    @Override
    public NguoiDung addUser(Map<String, String> params, MultipartFile avatar) {
        NguoiDung u = new NguoiDung();
        u.setUsername(params.get("username"));
        u.setPassword(params.get("password"));
        u.setEmail(params.get("email"));
        u.setHoTen(params.get("hoTen"));
        u.setSoDienThoai(params.get("soDienThoai"));
        u.setDiaChi(params.get("diaChi"));
        u.setVaiTro(params.get("vaiTro"));
        u.setNgayTao(new Date());
        u.setTrangThai(params.getOrDefault("trangThai", "CHO_DUYET"));

        u.setPassword(passEncoder.encode(u.getPassword()));  // Mã hóa mật khẩu

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(NguoiDungServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Gọi repository để lưu người dùng vào DB
        return nguoiDungRepo.addUser(u);  // Đảm bảo phương thức này hỗ trợ lưu user
    }
    
    @Override
    public boolean vaiTro(String username, String vaiTro) {
        NguoiDung nguoiDung = nguoiDungRepo.getByUsername(username);  // Lấy thông tin người dùng từ repository
        return nguoiDung != null && nguoiDung.getVaiTro().equals(vaiTro);  // Kiểm tra vai trò
    }

    @Override
    public boolean deleteUser(int id) {
        return nguoiDungRepo.deleteUser(id);
    }

    @Override
    public boolean authenticate(String username, String rawPassword) {
        NguoiDung u = this.getByUsername(username);
        if (u != null) {
            
            if (!"DUOC_DUYET".equals(u.getTrangThai())) {
            // Trạng thái không phải "DUOC_DUYET", không cho phép đăng nhập
            return false;
        }
//            System.out.println(" Testing login:");
//            System.out.println(" - Username: " + u.getUsername());
//            System.out.println(" - Raw password: " + rawPassword);
//            System.out.println(" - Stored hash: " + u.getPassword());
//            System.out.println(" - Password match: " + passEncoder.matches(rawPassword, u.getPassword()));
//
//            System.out.println("Raw: " + rawPassword);
//            System.out.println("Encoded: " + u.getPassword());
//            System.out.println("Match: " + passEncoder.matches(rawPassword, u.getPassword()));
        }
        return u != null && passEncoder.matches(rawPassword, u.getPassword());
    }

    @Override
    public List<NguoiDung> getAllUsers() {
        return nguoiDungRepo.getAllUsers();
    }
    
    @Override
    public boolean duyetNguoiDung(int userId) {
        return nguoiDungRepo.duyetNguoiDung(userId);  // Gọi phương thức approveUser từ repository
    } 
}
