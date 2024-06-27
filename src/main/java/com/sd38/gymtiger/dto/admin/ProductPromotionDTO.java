package com.sd38.gymtiger.dto.admin;

import java.math.BigDecimal;

public interface ProductPromotionDTO {
    Integer getId();
    String getCode();
    String getName();
    BigDecimal getMinProductPrice();
    BigDecimal getMaxProductPrice();
}
