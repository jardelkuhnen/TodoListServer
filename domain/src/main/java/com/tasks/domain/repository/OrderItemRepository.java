package com.tasks.domain.repository;

import com.tasks.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Modifying
    @Query("DELETE FROM OrderItem item WHERE item.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem item WHERE item.id = :id")
    void deleteByOrderItemId(@Param("id") Integer id);

    @Query("SELECT item FROM OrderItem item WHERE item.order.id = :orderId")
    List<OrderItem> findAllByOrderId(@Param("orderId") Integer orderId);
}
