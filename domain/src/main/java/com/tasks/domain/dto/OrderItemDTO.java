package com.tasks.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tasks.domain.model.Order;
import com.tasks.domain.model.OrderItem;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class OrderItemDTO {

    private Integer id;

    @NotEmpty(message = "Description not be empty")
    private String description;

    private Boolean isFinished;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;

    private Integer orderId;

    private Double price;

    public static List<OrderItem> off(List<OrderItemDTO> orderItensDTO) {
        return orderItensDTO.stream().map(OrderItemDTO::of).collect(Collectors.toList());
    }

    public static List<OrderItemDTO> of(List<OrderItem> orderItens) {
        return orderItens.stream().map(OrderItemDTO::of).collect(Collectors.toList());
    }

    public static OrderItem of(OrderItemDTO orderItemDTO) {
        OrderItem item = new OrderItem();
        item.setId(orderItemDTO.getId());
        item.setDescription(orderItemDTO.getDescription());
        item.setIsFinished(orderItemDTO.getIsFinished());
        item.setRegisterDate(orderItemDTO.getRegisterDate());
        item.setUpdateDate(orderItemDTO.getUpdateDate());
        item.setPrice(orderItemDTO.getPrice());

        return item;
    }

    public static OrderItemDTO of(OrderItem orderItem) {
        OrderItemDTO item = new OrderItemDTO();

        item.setId(orderItem.getId());
        item.setDescription(orderItem.getDescription());
        item.setIsFinished(orderItem.getIsFinished());
        item.setRegisterDate(orderItem.getRegisterDate());
        item.setUpdateDate(orderItem.getUpdateDate());
        item.setOrderId(orderItem.getOrder().getId());
        item.setPrice(orderItem.getPrice());

        return item;
    }

    public static OrderItem of(OrderItemDTO orderItemDTO, Order order) {
        OrderItem item = new OrderItem();

        item.setId(orderItemDTO.getId());
        item.setDescription(orderItemDTO.getDescription());
        item.setIsFinished(orderItemDTO.getIsFinished());
        item.setRegisterDate(orderItemDTO.getRegisterDate());
        item.setUpdateDate(orderItemDTO.getUpdateDate());
        item.setPrice(orderItemDTO.getPrice());
        item.setOrder(order);

        return item;
    }
}
