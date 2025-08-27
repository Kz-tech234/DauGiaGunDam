/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.ndk.pojo.NguoiDungCuocDauGia;
import com.ndk.repositories.NguoiDungCuocDauGiaRepository;
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
public class NguoiDungCuocDauGiaRepositoryImpl implements NguoiDungCuocDauGiaRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public NguoiDungCuocDauGia datGia(NguoiDungCuocDauGia p) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(p);
        return p;
    }

    @Override
    public List<NguoiDungCuocDauGia> getLichSuByPhien(int phienId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<NguoiDungCuocDauGia> q = session.createQuery(
            "FROM NguoiDungCuocDauGia WHERE phienDauGia.id = :phienId ORDER BY thoiGianDauGia DESC",
            NguoiDungCuocDauGia.class);
        q.setParameter("phienId", phienId);
        return q.getResultList();
    }

    @Override
    public List<NguoiDungCuocDauGia> getLichSuByNguoiDung(int nguoiDungId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<NguoiDungCuocDauGia> q = session.createQuery(
            "FROM NguoiDungCuocDauGia WHERE nguoiDung.id = :nguoiDungId ORDER BY thoiGianDauGia DESC",
            NguoiDungCuocDauGia.class);
        q.setParameter("nguoiDungId", nguoiDungId);
        return q.getResultList();
    }

    @Override
    public NguoiDungCuocDauGia getGiaCaoNhat(int phienId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<NguoiDungCuocDauGia> q = session.createQuery(
            "FROM NguoiDungCuocDauGia WHERE phienDauGia.id = :phienId ORDER BY giaDau DESC",
            NguoiDungCuocDauGia.class);
        q.setParameter("phienId", phienId);
        q.setMaxResults(1);
        return q.uniqueResult();
    }
    
    @Override
    public List<NguoiDungCuocDauGia> getByPhien(int phienId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = """
            FROM NguoiDungCuocDauGia pd
            WHERE pd.phienDauGia.id = :pid
            ORDER BY pd.giaDau DESC, pd.thoiGianDauGia ASC
        """;
        return s.createQuery(hql, NguoiDungCuocDauGia.class)
                .setParameter("pid", phienId)
                .getResultList();
    }
}
