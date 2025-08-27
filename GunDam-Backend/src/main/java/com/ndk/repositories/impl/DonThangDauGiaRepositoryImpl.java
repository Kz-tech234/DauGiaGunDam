/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.ndk.pojo.CuocDauGia;
import com.ndk.pojo.DonThangDauGia;
import com.ndk.pojo.NguoiDung;
import com.ndk.repositories.DonThangDauGiaRepository;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
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
public class DonThangDauGiaRepositoryImpl implements DonThangDauGiaRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    private Session s() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public DonThangDauGia findByPhien(CuocDauGia p) {
        String hql = "FROM DonThangDauGia d WHERE d.phienDauGia = :p";
        return s().createQuery(hql, DonThangDauGia.class)
                .setParameter("p", p)
                .uniqueResult();
    }

    @Override
    public List<DonThangDauGia> findByNguoiMua(NguoiDung u) {
        String hql = "FROM DonThangDauGia d WHERE d.nguoiMua = :u ORDER BY d.ngayTao DESC";
        return s().createQuery(hql, DonThangDauGia.class)
                .setParameter("u", u)
                .getResultList();
    }

    @Override
    public DonThangDauGia add(DonThangDauGia d) {
        s().persist(d);
        return d;
    }

    @Override
    public DonThangDauGia update(DonThangDauGia d) {
        s().merge(d);
        return d;
    }

    @Override
    public DonThangDauGia getById(Integer id) {
        return s().get(DonThangDauGia.class, id);
    }

    @Override
    public DonThangDauGia taoDon(CuocDauGia p) {
        if (p == null || p.getGiaChot() == null || p.getNguoiThangDauGia() == null) {
            return null;
        }

        DonThangDauGia existed = findByPhien(p);
        if (existed != null) {
            return existed;
        }

        DonThangDauGia d = new DonThangDauGia();
        d.setPhienDauGia(p);
        d.setNguoiMua(p.getNguoiThangDauGia());
        d.setSoTien(p.getGiaChot());
        d.setTrangThai(DonThangDauGia.TrangThai.PENDING);
        d.setPhuongThuc(DonThangDauGia.PhuongThuc.COD);

        s().persist(d);
        return d;
    }

    @Override
    public List<DonThangDauGia> DonQuaHanChuaThanhToan(DonThangDauGia.TrangThai trangThai, Date deadline) {
        String hql = "FROM DonThangnDauGia d WHERE d.trangThai = :trangThai AND d.ngayTao < :deadline";
        return s().createQuery(hql, DonThangDauGia.class)
                .setParameter("trangThai", trangThai)
                .setParameter("deadline", deadline)
                .getResultList();
    }

    @Override
    public void huyDon(int donId, String lyDo) {
        DonThangDauGia don = s().get(DonThangDauGia.class, donId);
        if (don != null && don.getTrangThai() == DonThangDauGia.TrangThai.SELLER_REVIEW) {
            don.setTrangThai(DonThangDauGia.TrangThai.CANCELLED);
            don.setLyDoTuNguoiBan(lyDo);
            s().merge(don);  // Cập nhật
        }
    }
}
