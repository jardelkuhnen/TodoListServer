package com.tasks.domain.repository;

import com.tasks.domain.dto.OrderDTO;
import com.tasks.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<OrderDTO> findByUserId(Long id, Pageable pageable);

    Order findByIdAndUserId(Integer orderId, Long userId);
}
