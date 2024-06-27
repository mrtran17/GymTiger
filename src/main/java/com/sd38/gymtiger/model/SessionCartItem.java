package com.sd38.gymtiger.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SessionCartItem {
    private Integer quantity;

    private BigDecimal price;

    private SessionCart cart;

    private ProductDetail productDetail;
}
