package com.sd38.gymtiger.controller.admin;

import com.sd38.gymtiger.dto.admin.thongke.SosanPhamBanDuoc;
import com.sd38.gymtiger.dto.admin.thongke.TKKhoangNgay;
import com.sd38.gymtiger.dto.admin.thongke.TKNam;
import com.sd38.gymtiger.dto.admin.thongke.TKNgay;
import com.sd38.gymtiger.dto.admin.thongke.TKSanPham;
import com.sd38.gymtiger.dto.admin.thongke.TKThang;
import com.sd38.gymtiger.dto.admin.thongke.TKTong;
import com.sd38.gymtiger.dto.admin.thongke.TKTrangThaiHoaDon;
import com.sd38.gymtiger.dto.admin.thongke.TKTuan;
import com.sd38.gymtiger.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/tiger/statistic")
public class ThongKeController {
    @Autowired
    private ThongKeService thongKeService;


    @GetMapping("/page")
    public String thongKeNgay(Model model, @RequestParam(name = "tungay", required = false) String tungay,
                              @RequestParam(name = "denngay", required = false) String denngay) {
        TKNgay tkNgay = thongKeService.getTKNgay();
        model.addAttribute("tkNgay", tkNgay);
        SosanPhamBanDuoc sosanPhamBanDuoc = thongKeService.getThongKeSanPhamBanDuocNgay();
        model.addAttribute("tksanphambanduocNgay", sosanPhamBanDuoc);
        TKTuan tkTuan = thongKeService.getTKTuan();
        model.addAttribute("tkTuan", tkTuan);
        SosanPhamBanDuoc sosanPhamBanDuocTuan = thongKeService.getThongKeSosanPhamBanDuocTuan();
        model.addAttribute("sosanPhamBanDuocTuan", sosanPhamBanDuocTuan);

        TKThang tkThang = thongKeService.getTKThang();
        model.addAttribute("tkThang", tkThang);
        SosanPhamBanDuoc sosanPhamBanDuocThang = thongKeService.getThongKeSosanPhamBanDuocThang();
        model.addAttribute("sosanPhamBanDuocThang", sosanPhamBanDuocThang);

        TKNam tkNam = thongKeService.getTKNam();
        model.addAttribute("tkNam", tkNam);
        SosanPhamBanDuoc sosanPhamBanDuocNam = thongKeService.getThongKeSosanPhamBanDuocNam();
        model.addAttribute("sosanPhamBanDuocNam", sosanPhamBanDuocNam);

        List<TKSanPham> tkSanPhamList = thongKeService.getTKSanPham();
        model.addAttribute("tkSanPhamList", tkSanPhamList);

        TKTong tkTong = thongKeService.getTKKhachHang();
        model.addAttribute("tkTong", tkTong);

        TKTong tkTongNV = thongKeService.getTKNhanVien();
        model.addAttribute("tkTongNV", tkTongNV);

        TKTong tkTongSanPham = thongKeService.getTKTongSanPham();
        model.addAttribute("tkTongSanPham", tkTongSanPham);

        TKTong tkTongDonHang = thongKeService.getTKTongDonHang();
        model.addAttribute("tkTongDonHang", tkTongDonHang);

        TKTong tkTongDoanhThu = thongKeService.getTKTongDoanhThu();
        model.addAttribute("tkTongDoanhThu", tkTongDoanhThu);

        List<TKTrangThaiHoaDon> tkTrangThaiHoaDonList = thongKeService.getTKTrangThaiHoaDon();
        model.addAttribute("tkTrangThaiHoaDonList", tkTrangThaiHoaDonList);
        if (tungay == null || tungay.isEmpty()) {
            tungay = "2024-01-01";
        }
        if (denngay == null || denngay.isEmpty()) {
            denngay = "2024-12-31";
        }
        model.addAttribute("tungay", tungay);
        model.addAttribute("denngay", denngay);

        List<TKKhoangNgay> thongKeKhoangNgayList = thongKeService.getTKSoLuongHD(tungay, denngay);
        model.addAttribute("thongKeKhoangNgayList", thongKeKhoangNgayList);
        List<SosanPhamBanDuoc> thongKeSanPhamBanDuocKhoangNgayList = thongKeService.getTKSosanPhamBanKhoangNgay(tungay, denngay);
        model.addAttribute("thongKeSanPhamBanDuocKhoangNgayList", thongKeSanPhamBanDuocKhoangNgayList);
        return "admin/statistic/statistic";
    }


}
