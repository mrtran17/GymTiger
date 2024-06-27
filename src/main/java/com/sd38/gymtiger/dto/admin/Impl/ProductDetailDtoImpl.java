package com.sd38.gymtiger.dto.admin.Impl;

import com.sd38.gymtiger.response.ProductDetailSearchResponse;
import com.sd38.gymtiger.dto.admin.ProductDetailDto;

public class ProductDetailDtoImpl {
    public static ProductDetailSearchResponse toProductSearchResponse(ProductDetailDto dto) {
        return ProductDetailSearchResponse.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .price(dto.getPrice())
                .priceDiscount(dto.getPriceDiscount())
                .sizeName(dto.getSizeName())
                .colorName(dto.getColorName())
                .quantity(dto.getQuantity())
                .build();
    }
}
