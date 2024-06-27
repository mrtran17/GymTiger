package com.sd38.gymtiger.dto.common.impl;

import com.sd38.gymtiger.response.ProductHomeResponse;
import com.sd38.gymtiger.dto.common.ProductHomeDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductHomeDtoImpl {
    Integer productId;

    String productName;

    BigDecimal price;

    public static ProductHomeDtoImpl toData(ProductHomeDto dto) {
        ProductHomeDtoImpl productHomeResponseDto = new ProductHomeDtoImpl();
        productHomeResponseDto.productName = dto.getProductName();
        productHomeResponseDto.productId = dto.getProductId();
        productHomeResponseDto.price = dto.getPrice();
        return productHomeResponseDto;
    }

    public ProductHomeResponse toResponse() {
        ProductHomeResponse response = new ProductHomeResponse();
        response.setId(this.productId);
        response.setName(this.productName);
        response.setPriceMax(this.price);
        response.setPriceMin(this.price);
        return response;
    }
}
