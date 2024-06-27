package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Integer> {
    Page<Form> findAllByStatusOrderByUpdateDateDesc(Pageable pageable, Integer status);
    List<Form> findAllByStatusOrderByUpdateDateDesc(Integer status);
    Page<Form> findAllByNameContainsAndStatusOrderByIdDesc(String name, Integer status, Pageable pageable);

    Form findByNameAndStatus(String name, Integer status);

    Optional<Form> findByCode(String code);

    Form findTopByOrderByIdDesc();
}
