package com.sd38.gymtiger.dto.admin.thongke;

import java.util.Date;

public interface PromotionSearchDTO {
    Integer getId();

    String getCode();

    String getName();
    Date getStartDate();

    Date getEndDate();

    Float getValue();

    Integer getStatus();
}
