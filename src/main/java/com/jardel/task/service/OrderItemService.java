package com.jardel.task.service;

import com.jardel.task.Response;
import com.jardel.task.dto.OrderDTO;
import com.jardel.task.dto.OrderItemDTO;
import com.jardel.task.exception.NotFoundException;
import com.jardel.task.model.Order;
import com.jardel.task.model.OrderItem;
import com.jardel.task.repository.OrderItemRepository;
import com.jardel.task.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderItem> save(List<OrderItemDTO> orderItens, Order order) {

        if(order.getId() != null) {
            this.orderItemRepository.deleteByOrderId(order.getId());
        }

        List<OrderItem> itens = OrderItemDTO.off(orderItens);
        itens.stream().map(item -> {
            item.setOrder(order);
            item.setRegisterDate(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());


        return this.orderItemRepository.saveAll(itens);
    }

    public List<OrderItem> put(List<OrderItemDTO> orderItemDTOS, Integer orderId) {

        List<OrderItem> itens = OrderItemDTO.off(orderItemDTOS);

        this.orderItemRepository.deleteByOrderId(orderId);

        itens = this.orderItemRepository.saveAll(itens);

        return itens;
    }

    public void delete(Integer orderId) {
        this.orderItemRepository.deleteByOrderId(orderId);
    }

    public void deleteByOrderItemId(Integer id) {
        this.orderItemRepository.deleteByOrderItemId(id);
    }

    public List<OrderItemDTO> getOrderItensByOrderId(Integer orderItem) {

        List<OrderItem> itens = this.orderItemRepository.findAllByOrderId(orderItem);

        List<OrderItemDTO> itensDTO = itens.stream().map(OrderItemDTO::of).collect(Collectors.toList());

        return itensDTO;

    }

    public OrderItemDTO save(OrderItemDTO orderItemDTO) throws NotFoundException {

        Optional<Order> orderOptional = this.orderRepository.findById(orderItemDTO.getOrderId());

        if(!orderOptional.isPresent()) {
            throw new NotFoundException(new Response("Order not found!", String.valueOf(HttpStatus.NOT_FOUND.value())));
        }

        Order order = orderOptional.get();

        OrderItem item = OrderItemDTO.of(orderItemDTO, order);
        item.setRegisterDate(LocalDateTime.now());

        item = this.orderItemRepository.save(item);

        return OrderItemDTO.of(item);

    }

    public OrderItemDTO put(OrderItemDTO orderItemDTO) throws NotFoundException {

        Optional<OrderItem> itemOptional = this.orderItemRepository.findById(orderItemDTO.getId());


        if(!itemOptional.isPresent()) {
            throw new NotFoundException(new Response("OrderItem not found!", String.valueOf(HttpStatus.NOT_FOUND.value())));
        }

        OrderItem item = itemOptional.get();

        item.setUpdateDate(LocalDateTime.now());
        item.setIsFinished(orderItemDTO.getIsFinished());
        item.setDescription(orderItemDTO.getDescription());

        this.orderItemRepository.save(item);

        return OrderItemDTO.of(item);
    }
}
