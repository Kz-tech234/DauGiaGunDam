/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.repositories.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndk.pojo.NguoiDung;
import com.ndk.pojo.TaiKhoanNganHang;
import com.ndk.repositories.TaiKhoanNganHangRepository;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Repository
@Transactional
public class TaiKhoanNganHangRepositoryImpl implements TaiKhoanNganHangRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private Cloudinary cloudinary;

    private Session s() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<TaiKhoanNganHang> findByNguoiBan(NguoiDung nd) {
        return s().createQuery("FROM TaiKhoanNganHang WHERE nguoiBan = :u", TaiKhoanNganHang.class)
                .setParameter("u", nd).getResultList();
    }

    @Override
    public TaiKhoanNganHang addTaiKhoan(NguoiDung u, String tenNguoiNhan, String nganHang, String soTaiKhoan, MultipartFile qrFile) {
        TaiKhoanNganHang tk = new TaiKhoanNganHang();
        tk.setNguoiBan(u);
        tk.setTenNguoiNhan(tenNguoiNhan);
        tk.setNganHang(nganHang);
        tk.setSoTaiKhoan(soTaiKhoan);
        tk.setMacDinh(true);

        // Upload ảnh QR lên Cloudinary
        if (qrFile != null && !qrFile.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(qrFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                tk.setQrUrl(uploadResult.get("secure_url").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        s().persist(tk);
        return tk;
    }
}
