/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.NguoiDungCuocDauGia;
import com.ndk.repositories.CuocDauGiaRepository;
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
public class CuocDauGiaRepositoryImpl implements CuocDauGiaRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public CuocDauGia themPhienDauGia(CuocDauGia p) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(p);
        return p;
    }

    @Override
    public List<CuocDauGia> getLayTatCaPhien() {
        Session session = this.factory.getObject().getCurrentSession();
        Query<CuocDauGia> q = session.createQuery("FROM CuocDauGia", CuocDauGia.class);
        return q.getResultList();
    }

    @Override
    public CuocDauGia getLayPhienTheoId(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(CuocDauGia.class, id);
    }

    @Override
    public boolean capNhatTrangThai(int id, String trangThai) {
        Session session = this.factory.getObject().getCurrentSession();
        CuocDauGia p = session.get(CuocDauGia.class, id);
        if (p != null) {
            p.setTrangThai(trangThai);
            session.update(p);
            return true;
        }
        return false;
    }

    @Override
    public CuocDauGia capNhatPhien(CuocDauGia p) {
        Session session = this.factory.getObject().getCurrentSession();
        session.update(p); // hoặc session.merge(p);
        return p;
    }

    @Override
    public boolean capNhatKetQuaPhien(int phienId) {
        Session session = this.factory.getObject().getCurrentSession();

        // Lấy phiên đấu giá
        CuocDauGia phien = session.get(CuocDauGia.class, phienId);
        if (phien == null) {
            return false;
        }

        // Kiểm tra nếu đã kết thúc và chưa có giá chốt
        Date now = new Date();
        if (phien.getThoiGianKetThuc().before(now) && phien.getGiaChot() == null) {
            // Lấy người có giá cao nhất
            String hql = "FROM NguoiDungCuocDauGia WHERE phienDauGia.id = :phienId ORDER BY giaDau DESC";
            Query<NguoiDungCuocDauGia> q = session.createQuery(hql, NguoiDungCuocDauGia.class);
            q.setParameter("phienId", phienId);
            q.setMaxResults(1);
            List<NguoiDungCuocDauGia> result = q.getResultList();

            if (!result.isEmpty()) {
                NguoiDungCuocDauGia topBid = result.get(0);

                phien.setGiaChot(topBid.getGiaDau());
                phien.setNguoiThangDauGia(topBid.getNguoiDung());
            }
            // Cập nhật trạng thái kết thúc dù có người đặt hay không
            phien.setTrangThai("da_ket_thuc");
            session.update(phien);
            return true;
        }

        return false;
    }
    
    public List<CuocDauGia> getPhienDauByNguoiDangId(int nguoiDangId) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "FROM CuocDauGia p WHERE p.nguoiDang.id = :nguoiDangId";
        Query<CuocDauGia> query = session.createQuery(hql, CuocDauGia.class);
        query.setParameter("nguoiDangId", nguoiDangId);
        return query.getResultList();
    }
}
