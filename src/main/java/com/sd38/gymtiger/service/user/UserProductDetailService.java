package com.sd38.gymtiger.service.user;

import com.sd38.gymtiger.model.ProductDetail;

import java.util.List;

public interface UserProductDetailService {
    List<ProductDetail> getAllProductDetail(Integer id);

    Integer getProductDetailId(Integer productId, Integer sizeId, Integer colorId);

    ProductDetail getProductDetailById(Integer id);

    ProductDetail getProductDetail(Integer productId, Integer sizeId, Integer colorId);
}
