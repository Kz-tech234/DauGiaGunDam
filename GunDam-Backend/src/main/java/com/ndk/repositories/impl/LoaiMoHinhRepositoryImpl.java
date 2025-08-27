/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.ndk.pojo.LoaiMoHinh;
import com.ndk.repositories.LoaiMoHinhRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Repository
@Transactional
public class LoaiMoHinhRepositoryImpl implements LoaiMoHinhRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public LoaiMoHinh add(LoaiMoHinh loaiSanPham) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(loaiSanPham);
        return loaiSanPham;
    }
    
    @Override
    public List<LoaiMoHinh> getLoaiSanPham() {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM LoaiMoHinh WHERE trangThai IN ('HOAT_DONG', 'BI_KHOA')";
        Query<LoaiMoHinh> query = session.createQuery(hql, LoaiMoHinh.class);
        return query.getResultList();
    }

    @Override
    public List<LoaiMoHinh> getLoaiSanPhamHoatDong() {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM LoaiMoHinh WHERE trangThai IN ('HOAT_DONG')";
        Query<LoaiMoHinh> query = session.createQuery(hql, LoaiMoHinh.class);
        return query.getResultList();
    }

    @Override
    public boolean deleteLoaiSanPham(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            LoaiMoHinh loai = session.get(LoaiMoHinh.class, id);
            if (loai != null) {
                session.delete(loai);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean khoaLoaiSanPham(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        LoaiMoHinh loai = session.get(LoaiMoHinh.class, id);
        if (loai != null) {
            loai.setTrangThai("BI_KHOA");
            session.update(loai);
            return true;
        }
        return false;
    }

    @Override
    public boolean moKhoaLoaiSanPham(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        LoaiMoHinh loai = session.get(LoaiMoHinh.class, id);
        if (loai != null) {
            loai.setTrangThai("HOAT_DONG");
            session.update(loai);
            return true;
        }
        return false;
    }
}
