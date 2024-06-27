package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

    List<Size> findAllByStatusOrderByUpdateDateDesc(Integer status);

    // Pagination
    Page<Size> findAllByStatusOrderByUpdateDateDesc(Pageable pageable, Integer status);

    //Search
    Page<Size> findAllByNameContainsAndStatusOrderByIdDesc(String name, Integer status, Pageable pageable);

//    Optional<Size> findByName(String name);

    Size findByNameAndStatus(String name, Integer status);

    Size findTopByOrderByIdDesc();

}
