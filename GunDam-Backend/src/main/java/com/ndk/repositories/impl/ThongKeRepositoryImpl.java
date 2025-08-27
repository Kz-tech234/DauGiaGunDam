/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.ndk.dto.ThongKeDTO;
import com.ndk.repositories.CuocDauGiaRepository;
import com.ndk.repositories.ThongKeRepository;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Repository
public class ThongKeRepositoryImpl implements ThongKeRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ThongKeDTO> thongKeTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
        Session s = factory.getObject().getCurrentSession();

        String sql = """
            SELECT DATE(ngayThanhToan) AS ngay,
                   COALESCE(SUM(soTien), 0) AS tong_tien,
                   COUNT(*) AS so_don
            FROM donthanhtoan_daugia
            WHERE trangThai = 'PAID'
              AND ngayThanhToan IS NOT NULL
              AND DATE(ngayThanhToan) BETWEEN :tu AND :den
            GROUP BY DATE(ngayThanhToan)
            ORDER BY DATE(ngayThanhToan)
        """;

        NativeQuery<Object[]> q = s.createNativeQuery(sql);
        q.setParameter("tu", Date.valueOf(tuNgay));
        q.setParameter("den", Date.valueOf(denNgay));

        List<Object[]> rows = q.getResultList();
        List<ThongKeDTO> kq = new ArrayList<>();
        for (Object[] r : rows) {
            LocalDate ngay = ((Date) r[0]).toLocalDate();
            BigDecimal tongTien = (BigDecimal) r[1];
            long soDon = ((Number) r[2]).longValue();
            kq.add(new ThongKeDTO(ngay, tongTien, soDon));
        }
        return kq;
    }
}
