package com.sd38.gymtiger.dto.admin.thongke;

import java.math.BigDecimal;
import java.util.Date;

public interface VoucherSearchDTO {
    Integer getId();

    String getCode();

    String getName();
    Integer getQuantity();
    BigDecimal getValue();
    BigDecimal getMinimumPrice();
    Date getStartDate();

    Date getEndDate();

    Integer getStatus();
}
