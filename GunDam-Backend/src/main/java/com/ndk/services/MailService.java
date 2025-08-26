/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.services;

/**
 *
 * @author Tran Quoc Phong
 */
public interface MailService {
    void sendApprovalEmail(String toEmail, String hoTen);
    void sendWinnerEmail(String toEmail, String hoTen, String tenSanPham, Number giaThang);
}
