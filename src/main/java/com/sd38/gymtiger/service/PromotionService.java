package com.sd38.gymtiger.service;

import com.sd38.gymtiger.dto.admin.PromotionDetailDTO;
import com.sd38.gymtiger.dto.admin.thongke.PromotionSearchDTO;
import com.sd38.gymtiger.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface PromotionService {
    void scheduleDeleteExpiredPromotions();

    List<Promotion> getExpiredPromotions();

    void processExpiredPromotion(Promotion promotion);

    List<Promotion> getAll();

    Page<Promotion> getAllPT(Integer page);

    Boolean add(Promotion promotion);

    Boolean update(Promotion promotion, Integer id);

    Promotion delete(Integer id);

    Promotion getOne(Integer id);

    Page<Promotion> search(String name, int page);

    Page<PromotionSearchDTO> findAll(Pageable pageable);

    Page<PromotionSearchDTO> searchPromotion(
            String code,
            Date ngayTaoStart,
            Date ngayTaoEnd,
            Integer status,
            String name,
            int page);
    List<PromotionDetailDTO> getPromotionDetailsByPromotionId(Integer promotionId);


}
