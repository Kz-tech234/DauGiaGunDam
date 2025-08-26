/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndk.pojo.LoaiMoHinh;
import com.ndk.pojo.MoHinh;
import com.ndk.pojo.NguoiDung;
import com.ndk.repositories.MoHinhRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Tran Quoc Phong
 */
@Repository
@Transactional
public class MoHinhRepositoryImpl implements MoHinhRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public MoHinh addSanPham(String tenSanPham, String moTa, BigDecimal giaKhoiDiem, BigDecimal buocNhay, Date thoiGianKetThuc, int loaiSanPhamId, String username, MultipartFile avatar) {
        Session session = this.factory.getObject().getCurrentSession();
        // Tạo đối tượng SanPham từ thông tin trong params
        MoHinh sanPham = new MoHinh();
        sanPham.setTenSanPham(tenSanPham);
        sanPham.setMoTa(moTa);
        sanPham.setGiaKhoiDiem(giaKhoiDiem);
        sanPham.setBuocNhay(buocNhay);
        sanPham.setThoiGianKetThuc(thoiGianKetThuc);
        sanPham.setNgayDang(new java.util.Date());
        sanPham.setTrangThai("CHO_DUYET");

        // Lấy đối tượng LoaiSanPham từ ID
        LoaiMoHinh loaiSanPham = session.get(LoaiMoHinh.class, loaiSanPhamId);
        sanPham.setLoaiSanPham(loaiSanPham);

        // Lấy đối tượng NguoiDung từ username
        Query<NguoiDung> query = session.createQuery("FROM NguoiDung WHERE username = :un", NguoiDung.class);
        query.setParameter("un", username);
        NguoiDung nguoiDung = query.uniqueResult(); 
        sanPham.setNguoiDung(nguoiDung);
        
        System.out.println("SanPham debug:");
        System.out.println("Ten: " + tenSanPham);
        System.out.println("NguoiDung: " + (nguoiDung != null ? nguoiDung.getUsername() : "NULL"));
        System.out.println("LoaiSanPham: " + (loaiSanPham != null ? loaiSanPham.getTenLoai() : "NULL"));

        // Upload ảnh nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                sanPham.setHinhAnh(uploadResult.get("secure_url").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Lưu sản phẩm vào DB
        session.persist(sanPham);
        return sanPham;
    }

    @Override
    public List<MoHinh> getAll() {
        Session session = this.factory.getObject().getCurrentSession();
        Query<MoHinh> q = session.createQuery("FROM SanPham", MoHinh.class);
        return q.getResultList();
    }
    
    @Override
    public boolean updateTrangThai(int id, String trangThai) {
        Session session = this.factory.getObject().getCurrentSession();
        MoHinh sanPham = session.get(MoHinh.class, id);
        if (sanPham != null) {
            sanPham.setTrangThai(trangThai);  
            session.update(sanPham);
            return true;
        }
        return false;
    }

    @Override
    public MoHinh getById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(MoHinh.class, id);
    }

    @Override
    public boolean delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        MoHinh sp = session.get(MoHinh.class, id);
        if (sp != null) {
            session.delete(sp);
            return true;
        }
        return false;
    }
    
    @Override
    public List<MoHinh> getSanPhamsTheoTrangThai(String trangThai) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<MoHinh> q = session.createQuery("FROM SanPham WHERE trangThai = :tt", MoHinh.class);
        q.setParameter("tt", trangThai);
        return q.getResultList();
    }
}
