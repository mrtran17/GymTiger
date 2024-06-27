package com.sd38.gymtiger.dto.admin.thongke;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public interface TKThang {
    Integer getSoLuongThanhCong();

    BigDecimal getDoanhThu();

    Integer getSoLuongHuy();

}
