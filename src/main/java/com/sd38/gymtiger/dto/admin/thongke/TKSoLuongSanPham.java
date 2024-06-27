package com.sd38.gymtiger.dto.admin.thongke;


import java.math.BigDecimal;
import java.sql.Date;


public interface TKSoLuongSanPham {
    Date getOrderDate();

    Integer getSoLuong();

    BigDecimal getDoanhThu();
}
