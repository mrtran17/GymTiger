package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.dto.admin.thongke.TKTong;
import com.sd38.gymtiger.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongKeTongSanPham extends JpaRepository<ProductDetail,Integer> {
    @Query(value = "SELECT SUM(Quantity) AS SoLuong\n" +
            "FROM ProductDetail\n" +
            "WHERE Status = 1", nativeQuery = true)
    public TKTong getTKTongSanPham();
}
