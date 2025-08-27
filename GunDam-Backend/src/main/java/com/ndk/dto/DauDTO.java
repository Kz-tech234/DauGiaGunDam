/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Nguyen Dang Khoi
 */
public class DauDTO {
    private String hoTen;
    private String username;
    private String avatar;
    private BigDecimal giaDau;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime thoiGian;

    private boolean caoNhat;

    public DauDTO() {
        
    }
    
    public DauDTO(String hoTen, String username, String avatar, BigDecimal giaDau,
                  LocalDateTime thoiGian, boolean caoNhat) {
        this.hoTen = hoTen;
        this.username = username;
        this.avatar = avatar;
        this.giaDau = giaDau;
        this.thoiGian = thoiGian;
        this.caoNhat = caoNhat;
    }
    
    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BigDecimal getGiaDau() {
        return giaDau;
    }

    public void setGiaDau(BigDecimal giaDau) {
        this.giaDau = giaDau;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public boolean isCaoNhat() {
        return caoNhat;
    }

    public void setCaoNhat(boolean caoNhat) {
        this.caoNhat = caoNhat;
    }
}
