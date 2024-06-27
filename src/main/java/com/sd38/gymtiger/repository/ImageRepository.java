package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.Image;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findAllByStatus(Integer status);

    Image findByIdAndStatus(Integer id, Integer status);

    List<Image> findAllByProduct_Id(Integer productId);

    Page<Image> findAllByProductIdAndStatusOrderByUpdateDateDesc(Integer productId, Integer status, Pageable pageable);

    Page<Image> getAllImageByProduct_IdAndStatusOrderByUpdateDateDesc(Pageable pageable, Integer id, Integer status);

    Image findTopByProductIdAndStatusOrderByUpdateDateDesc(Integer productId, Integer status);

    List<Image> findAllByProduct_IdAndStatusOrderByUpdateDateDesc(Integer productId, Integer status);

    Image findTopByProduct_IdAndStatusOrderByUpdateDateDesc(Integer productId, Integer status);

}
