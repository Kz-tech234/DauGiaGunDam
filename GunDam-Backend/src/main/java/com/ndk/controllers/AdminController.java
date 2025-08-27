/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.controllers;

import com.ndk.dto.ThongKeDTO;
import com.ndk.pojo.NguoiDung;
import com.ndk.services.MailService;
import com.ndk.services.NguoiDungService;
import com.ndk.services.ThongKeService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Nguyen Dang Khoi
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("")
    public String adminView(Model model) {
        List<NguoiDung> users = nguoiDungService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";  // admin.html
    }

    @GetMapping("/duyetNguoiDung")
    public String duyetNguoiDung(Model model) {
        List<NguoiDung> users = nguoiDungService.getAllUsers();  // Lấy danh sách người dùng cần duyệt
        model.addAttribute("users", users);
        return "duyetNguoiDung"; // Trả về view "duyetNguoiDung.html"
    }

    @PostMapping("/duyetNguoiDung")
    public String approveUser(@RequestParam("userId") int userId, RedirectAttributes redirectAttrs) {
        boolean result = nguoiDungService.duyetNguoiDung(userId);

        if (result) {
            try {
                NguoiDung user = nguoiDungService.getById(userId);
                if (user != null && user.getEmail() != null && !user.getEmail().isBlank()) {
                    mailService.sendApprovalEmail(user.getEmail(), user.getHoTen());
                }
                redirectAttrs.addFlashAttribute("message", "Duyệt người dùng thành công! Đã gửi email thông báo.");
            } catch (Exception ex) {
                // Không để việc gửi mail làm fail luồng duyệt — log + báo nhẹ
                ex.printStackTrace();
                redirectAttrs.addFlashAttribute("message", "Duyệt người dùng thành công! (Gửi email thất bại, vui lòng kiểm tra log)");
            }
        } else {
            redirectAttrs.addFlashAttribute("message", "Duyệt người dùng thất bại!");
        }

        return "redirect:/admin/duyetNguoiDung";
    }

    @GetMapping("/themNguoiDung")
    public String showAddUserForm(Model model) {
        model.addAttribute("nguoiDung", new NguoiDung());
        return "themUser"; // tên file addUser.html
    }

    @PostMapping("/themNguoiDung")
    public String addUser(@RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            RedirectAttributes redirectAttrs) {

        // Kiểm tra giá trị vaiTro và gán mặc định nếu không hợp lệ
        String vaiTro = params.get("vaiTro");

        // Kiểm tra xem vaiTro có hợp lệ không
        if (!Arrays.asList("ROLE_ADMIN", "ROLE_NGUOIBAN", "ROLE_NGUOIMUA").contains(vaiTro)) {
            vaiTro = "ROLE_NGUOIMUA";  // Gán mặc định nếu không hợp lệ
        }

        // Đảm bảo giá trị vaiTro đã được gán chính xác
        params.put("vaiTro", vaiTro);

        // Nếu là admin thì gán trạng thái được duyệt luôn
        if ("ROLE_ADMIN".equals(vaiTro)) {
            params.put("trangThai", "DUOC_DUYET");
        } else {
            params.put("trangThai", "CHO_DUYET");
        }

        // Thực hiện thêm người dùng
        nguoiDungService.addUser(params, avatar);
        redirectAttrs.addFlashAttribute("message", "Thêm người dùng thành công!");
        return "redirect:/admin";
    }
    
    @PostMapping("/khoa-user")
    public String khoaUser(@RequestParam("userId") int id, RedirectAttributes redirectAttrs) {
        boolean result = nguoiDungService.khoaUser(id);
        redirectAttrs.addFlashAttribute("message", result ? "Đã khóa tài khoản thành công!" : "Khóa tài khoản thất bại!");
        return "redirect:/admin";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") int id, RedirectAttributes redirectAttrs) {
        try {
            boolean result = nguoiDungService.deleteUser(id);

            if (result) {
                redirectAttrs.addFlashAttribute("message", "Xóa người dùng thành công!");
            } else {
                redirectAttrs.addFlashAttribute("message", "Không thể xóa người dùng này được, chỉ có thể khóa.");
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // hoặc log.error
            redirectAttrs.addFlashAttribute("message", "Không thể xóa người dùng này được, chỉ có thể khóa.");
        }

        return "redirect:/admin";
    }
    
    @PostMapping("/mo-khoa-user")
    public String moKhoaUser(@RequestParam("userId") int id, RedirectAttributes redirectAttrs) {
        boolean result = nguoiDungService.moKhoaUser(id);
        redirectAttrs.addFlashAttribute("message", result ? "Tài khoản đã được mở khóa!" : "Mở khóa thất bại!");
        return "redirect:/admin";
    }

    @GetMapping("/thongKeDauGia")
    public String thongKeDauGia(
            Model model,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate == null) {
            startDate = endDate.minusDays(30);
        }
        if (endDate.isBefore(startDate)) {
            var t = startDate;
            startDate = endDate;
            endDate = t;
        }

        List<ThongKeDTO> ds = thongKeService.thongKeTheoNgay(startDate, endDate);

        model.addAttribute("auctionStats", ds);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        var tongDoanhThu = ds.stream()
                .map(ThongKeDTO::getTongTien)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        var tongDon = ds.stream().mapToLong(ThongKeDTO::getSoDonThanhToan).sum();

        model.addAttribute("totalRevenue", tongDoanhThu);
        model.addAttribute("totalOrders", tongDon);

        return "thongKeDauGia"; 
    }
}
