package com.realtypro.repository;

import com.realtypro.schema.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT SUM(s.saleAmount) FROM Sale s WHERE s.agent.userId = :userId")
    Double sumSalesByAgent(@Param("userId") Long userId);

    @Query("SELECT SUM(s.saleAmount) FROM Sale s WHERE s.manager.userId = :userId")
    Double sumSalesByManager(@Param("userId") Long userId);

}
