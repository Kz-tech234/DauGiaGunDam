/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Tran Quoc Phong
 */
@Entity
@Table(name = "mohinhs")
public class MoHinh implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoiDung_id")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "loaiSanPham_id")
    private LoaiMoHinh loaiSanPham;

    private String tenSanPham;
    private String moTa;
    private String hinhAnh;
    private BigDecimal giaKhoiDiem;
    private BigDecimal buocNhay;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayDang;
    private Date thoiGianKetThuc;
    private String trangThai;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public LoaiMoHinh getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiMoHinh loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public BigDecimal getGiaKhoiDiem() {
        return giaKhoiDiem;
    }

    public void setGiaKhoiDiem(BigDecimal giaKhoiDiem) {
        this.giaKhoiDiem = giaKhoiDiem;
    }
    
    public BigDecimal getBuocNhay() {
        return buocNhay;
    }

    public void setBuocNhay(BigDecimal buocNhay) {
        this.buocNhay = buocNhay;
    }

    public Date getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(Date ngayDang) {
        this.ngayDang = ngayDang;
    }
    
    public Date getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(Date thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }
    
    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
