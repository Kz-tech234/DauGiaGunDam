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
import java.util.Date;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Entity
@Table(name = "theodoimohinh")
public class TheoDoiMoHinh implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoiDung_id")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "phienDauGia_id")
    private CuocDauGia phienDauGia;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTheoDoi;
    
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

    public CuocDauGia getPhienDauGia() {
        return phienDauGia;
    }

    public void setPhienDauGia(CuocDauGia phienDauGia) {
        this.phienDauGia = phienDauGia;
    }
    
    public Date getNgayTheoDoi() {
        return ngayTheoDoi;
    }

    public void setNgayTheoDoi(Date ngayTheoDoi) {
        this.ngayTheoDoi = ngayTheoDoi;
    }
}
