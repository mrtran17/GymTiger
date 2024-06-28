package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.dto.admin.thongke.VoucherSearchDTO;
import com.sd38.gymtiger.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Voucher findTopByOrderByIdDesc();

    @Query("select o from Voucher o where o.status=?1")
    List<Voucher> findAllByStatusOrderByIdDesc(Integer status);

    List<Voucher> findByEndDateBeforeAndStatus(Date endDate, Integer status);

    @Query("SELECT v FROM Voucher v WHERE v.status IN (1, 2) ORDER BY v.id DESC")
    Page<Voucher> findAllByStatusOrderByIdDesc(Pageable pageable);

    Page<Voucher> findAllByNameContainsAndStatusOrderByIdDesc(String name, Integer status, Pageable pageable);

    @Query("SELECT v FROM Voucher v WHERE v.status = 1 AND v.minimumPrice <= :cartPrice ORDER BY v.value DESC")
    List<Voucher> getVoucherByCartPrice(@Param("cartPrice") BigDecimal cartPrice);

    @Query(value = "SELECT v.id AS id, v.code AS code, v.name AS name, v.value AS value,v.quantity AS quantity, v.minimumPrice AS minimumPrice, v.startDate AS startDate, v.endDate AS endDate, v.status AS status " +
            "FROM Voucher v " +
            "WHERE (v.code IS NULL OR v.code LIKE CONCAT('%', :code, '%')) " +
            "AND (:ngayTaoStart IS NULL OR :ngayTaoEnd IS NULL OR (v.endDate BETWEEN :ngayTaoStart AND :ngayTaoEnd)) " +
            "AND (v.status IS NULL OR v.status = :status) " +
            "AND (v.name IS NULL OR v.name LIKE CONCAT('%', :name, '%')) ")
    Page<VoucherSearchDTO> searchVoucher(
            @Param("code") String code,
            @Param("ngayTaoStart") Date ngayTaoStart,
            @Param("ngayTaoEnd") Date ngayTaoEnd,
            @Param("status") Integer status,
            @Param("name") String name,
            Pageable pageable);

    Voucher findByNameAndStatus(String name, Integer status);
}
