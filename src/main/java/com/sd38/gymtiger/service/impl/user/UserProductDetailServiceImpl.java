package com.sd38.gymtiger.service.impl.user;

import com.sd38.gymtiger.repository.user.UserProductDetailRepository;
import com.sd38.gymtiger.service.user.UserProductDetailService;
import com.sd38.gymtiger.model.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserProductDetailServiceImpl implements UserProductDetailService {
    @Autowired
    private UserProductDetailRepository userProductDetailRepository;

    @Override
    public List<ProductDetail> getAllProductDetail(Integer id) {
        return userProductDetailRepository.getAllProductDetail(id);
    }

    @Override
    public Integer getProductDetailId(Integer productId, Integer sizeId, Integer colorId) {
        return userProductDetailRepository.getProductDetailId(productId, sizeId, colorId);
    }

    @Override
    public ProductDetail getProductDetailById(Integer id) {
        return userProductDetailRepository.findById(id).orElse(null);
    }

    @Override
    public ProductDetail getProductDetail(Integer productId, Integer sizeId, Integer colorId) {
        return userProductDetailRepository.getProductDetail(productId, sizeId, colorId);
    }
}
