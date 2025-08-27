/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Entity 
@Table(name="donThangDauGia")
public class DonThangDauGia implements Serializable{
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name="phienDauGia_id")
    private CuocDauGia phienDauGia;

    @ManyToOne
    @JoinColumn(name="nguoiMua_id")
    private NguoiDung nguoiMua;

    private BigDecimal soTien;

    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.PENDING;

    @Enumerated(EnumType.STRING)
    private PhuongThuc phuongThuc = PhuongThuc.COD;

    private String hoTenNhan;
    private String soDienThoai;
    private String diaChiNhan;
    private String ghiChu;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThanhToan;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngaySellerDuyet;

    public enum TrangThai { PENDING, SELLER_REVIEW, PAID, CANCELLED }
    private String lyDoTuNguoiBan;
    public enum PhuongThuc { COD, BANK }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public CuocDauGia getPhienDauGia() {
        return phienDauGia;
    }
    public void setPhienDauGia(CuocDauGia phienDauGia) {
        this.phienDauGia = phienDauGia;
    }

    public NguoiDung getNguoiMua() {
        return nguoiMua;
    }
    public void setNguoiMua(NguoiDung nguoiMua) {
        this.nguoiMua = nguoiMua;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }
    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }

    public PhuongThuc getPhuongThuc() {
        return phuongThuc;
    }
    public void setPhuongThuc(PhuongThuc phuongThuc) {
        this.phuongThuc = phuongThuc;
    }
    
    public String getLyDoTuNguoiBan() {
        return lyDoTuNguoiBan;
    }
    public void setLyDoTuNguoiBan(String lyDoTuNguoiBan) {
        this.lyDoTuNguoiBan = lyDoTuNguoiBan;
    }

    public String getHoTenNhan() {
        return hoTenNhan;
    }
    public void setHoTenNhan(String hoTenNhan) {
        this.hoTenNhan = hoTenNhan;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChiNhan() {
        return diaChiNhan;
    }
    public void setDiaChiNhan(String diaChiNhan) {
        this.diaChiNhan = diaChiNhan;
    }

    public String getGhiChu() {
        return ghiChu;
    }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Date getNgayTao() {
        return ngayTao;
    }
    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }
    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }
    
    public Date getNgaySellerDuyet() {
        return ngaySellerDuyet;
    }

    public void setNgaySellerDuyet(Date ngaySellerDuyet) {
        this.ngaySellerDuyet = ngaySellerDuyet;
    }
}
