package com.sd38.gymtiger.service;

import com.sd38.gymtiger.dto.admin.thongke.SosanPhamBanDuoc;
import com.sd38.gymtiger.dto.admin.thongke.TKKhoangNgay;
import com.sd38.gymtiger.dto.admin.thongke.TKNam;
import com.sd38.gymtiger.dto.admin.thongke.TKNgay;
import com.sd38.gymtiger.dto.admin.thongke.TKSLThang;
import com.sd38.gymtiger.dto.admin.thongke.TKSanPham;
import com.sd38.gymtiger.dto.admin.thongke.TKSoLuongSanPham;
import com.sd38.gymtiger.dto.admin.thongke.TKThang;
import com.sd38.gymtiger.dto.admin.thongke.TKTong;
import com.sd38.gymtiger.dto.admin.thongke.TKTrangThaiHoaDon;
import com.sd38.gymtiger.dto.admin.thongke.TKTuan;

import java.util.List;

public interface ThongKeService {
    public TKNgay getTKNgay();
    public SosanPhamBanDuoc getThongKeSanPhamBanDuocNgay();

    public TKTuan getTKTuan();
    public SosanPhamBanDuoc getThongKeSosanPhamBanDuocTuan();

    public TKThang getTKThang();
    public SosanPhamBanDuoc getThongKeSosanPhamBanDuocThang();

    public TKNam getTKNam();
    public SosanPhamBanDuoc getThongKeSosanPhamBanDuocNam();

    public TKSLThang getTKSLThang();

    public List<TKKhoangNgay> getTKSoLuongHD(String tungay, String denngay);
    public List<SosanPhamBanDuoc> getTKSosanPhamBanKhoangNgay(String tungay, String denngay);

    public List<TKSoLuongSanPham> getTKSoLuongSanPham(String tungay, String denngay);

    public List<TKSanPham> getTKSanPham();

    public List<TKTrangThaiHoaDon> getTKTrangThaiHoaDon();

    public TKTong getTKKhachHang();
    public TKTong getTKTongSanPham();
    public TKTong getTKTongDonHang();

    public TKTong getTKTongDoanhThu();

    public TKTong getTKNhanVien();
}
