package com.sd38.gymtiger.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertiesResponse {
    private String code;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal priceDiscount;
    private Integer id;
}

