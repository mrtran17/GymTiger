package com.sd38.gymtiger.service;

import com.sd38.gymtiger.dto.admin.ProductPromotionDTO;
import com.sd38.gymtiger.dto.admin.PromotionDetailDTO;
import com.sd38.gymtiger.model.Product;
import com.sd38.gymtiger.model.ProductDetail;
import com.sd38.gymtiger.model.PromotionDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PromotionDetailService {
    List<PromotionDetail> getAllPromotionDetail();

    void save(Product product);


    Page<PromotionDetail> getAllPT(Integer page);

    PromotionDetail add(PromotionDetail promotionDetail);

    PromotionDetail update(PromotionDetail promotionDetail, Integer id);

    PromotionDetail delete(Integer id);


    PromotionDetail getOne(Integer id);


    List<Product> getAll();

    List<ProductDetail> getAllPrDT();

    List<Product> getProductsByIds(List<Integer> productIds);

    Boolean existsByProductIdAndStatus(Integer productId, Integer status);

    Page<PromotionDetailDTO> All(Integer page);

    List<ProductDetail> findByProductId(Integer productId);

    List<ProductPromotionDTO> getAllProductPromotionDTO();

    PromotionDetail findByProductIdAndPromotionId(Integer productId, Integer promotionId);
}
