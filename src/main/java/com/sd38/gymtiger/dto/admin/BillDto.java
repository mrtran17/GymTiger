package com.sd38.gymtiger.dto.admin;

import java.math.BigDecimal;
import java.util.Date;

public interface BillDto {
    Integer getId();
    String getCode();
    String getCustomerName();
    String getPhoneNumber();
    Date getCreateDate();
    BigDecimal getTotalPrice();
    Integer getType();
    Integer getStatus();
}

