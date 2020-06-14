package com.tasks.domain.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tasks.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class OrderDTO {

    private Integer id;

    @NotEmpty(message = "Description not be empty")
    private String description;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;

    @NotEmpty(message = "Itens cannot be empty")
    private List<OrderItemDTO> itens;

    public static Order of(OrderDTO orderDTO) {

//        List<OrderItem> orderItens = OrderItemDTO.off(orderDTO.getItens());

        Order order = new Order(orderDTO.getId(), orderDTO.getDescription(), orderDTO.getRegisterDate(), orderDTO.getUpdateDate());

        return order;
    }

    public static OrderDTO of(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .description(order.getDescription())
                .registerDate(order.getRegisterDate())
                .updateDate(order.getUpdateDate())

                .itens(OrderItemDTO.of(order.getItens())).build();
    }
}
