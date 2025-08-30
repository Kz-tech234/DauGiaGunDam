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
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Entity 
@Table(name="cuocdaugia")
public class CuocDauGia implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoiDang_id")
    private NguoiDung nguoiDang;

    @ManyToOne
    @JoinColumn(name = "sanPham_id")
    private MoHinh sanPham;

    @Temporal(TemporalType.TIMESTAMP)
    private Date thoiGianBatDau;

    @Temporal(TemporalType.TIMESTAMP)
    private Date thoiGianKetThuc;

    private String trangThai;
    private BigDecimal giaChot;

    @ManyToOne
    @JoinColumn(name = "nguoiThangDauGia_id")
    private NguoiDung nguoiThangDauGia;

    private Boolean daThongBaoKQ;

    @Transient
    private BigDecimal giaHienTai;

    @Transient
    private DonThangDauGia donThanhToan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NguoiDung getNguoiDang() {
        return nguoiDang;
    }

    public void setNguoiDang(NguoiDung nguoiDang) {
        this.nguoiDang = nguoiDang;
    }

    public MoHinh getSanPham() {
        return sanPham;
    }

    public void setSanPham(MoHinh sanPham) {
        this.sanPham = sanPham;
    }

    public Date getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(Date thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
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

    public BigDecimal getGiaChot() {
        return giaChot;
    }

    public void setGiaChot(BigDecimal giaChot) {
        this.giaChot = giaChot;
    }

    public NguoiDung getNguoiThangDauGia() {
        return nguoiThangDauGia;
    }

    public void setNguoiThangDauGia(NguoiDung nguoiThangDauGia) {
        this.nguoiThangDauGia = nguoiThangDauGia;
    }

    public Boolean getDaThongBaoKQ() {
        return daThongBaoKQ;
    }

    public void setDaThongBaoKQ(Boolean daThongBaoKQ) {
        this.daThongBaoKQ = daThongBaoKQ;
    }

    public BigDecimal getGiaHienTai() {
        return giaHienTai;
    }

    public void setGiaHienTai(BigDecimal giaHienTai) {
        this.giaHienTai = giaHienTai;
    }
    
    public DonThangDauGia getDonThanhToan() { 
        return donThanhToan; 
    }
    
    public void setDonThanhToan(DonThangDauGia don) { 
        this.donThanhToan = don; 
    }

}
