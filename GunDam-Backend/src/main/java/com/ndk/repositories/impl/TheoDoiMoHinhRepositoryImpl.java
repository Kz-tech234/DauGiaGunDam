/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.TheoDoiMoHinh;
import com.ndk.repositories.CuocDauGiaRepository;
import com.ndk.repositories.TheoDoiMoHinhRepository;
import java.util.Date;
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
public class TheoDoiMoHinhRepositoryImpl implements TheoDoiMoHinhRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public boolean kiemTraTheoDoi(int nguoiDungId, int phienDauGiaId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(*) FROM TheoDoiMoHinh WHERE nguoiDung.id = :uid AND phienDauGia.id = :pid");
        q.setParameter("uid", nguoiDungId);
        q.setParameter("pid", phienDauGiaId);
        return ((Long) q.getSingleResult()) > 0;
    }

    @Override
    public void themTheoDoi(int nguoiDungId, int phienDauGiaId) {
        Session s = factory.getObject().getCurrentSession();
        TheoDoiMoHinh td = new TheoDoiMoHinh();

        NguoiDung nd = s.get(NguoiDung.class, nguoiDungId);
        CuocDauGia pdg = s.get(CuocDauGia.class, phienDauGiaId);

        td.setNguoiDung(nd);
        td.setPhienDauGia(pdg);
        td.setNgayTheoDoi(new Date());
        s.save(td);
    }

    @Override
    public void xoaTheoDoi(int nguoiDungId, int phienDauGiaId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("DELETE FROM TheoDoiMoHinh WHERE nguoiDung.id = :uid AND phienDauGia.id = :pid");
        q.setParameter("uid", nguoiDungId);
        q.setParameter("pid", phienDauGiaId);
        q.executeUpdate();
    }

    @Override
    public List<TheoDoiMoHinh> layTheoDoiTheoNguoiDung(int nguoiDungId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM TheoDoiMoHinh WHERE nguoiDung.id = :uid");
        q.setParameter("uid", nguoiDungId);
        return q.getResultList();
    }
}
