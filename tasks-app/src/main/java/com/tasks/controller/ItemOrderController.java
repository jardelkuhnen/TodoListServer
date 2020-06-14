package com.tasks.controller;


import com.tasks.domain.dto.OrderItemDTO;
import com.tasks.domain.exception.NotFoundException;
import com.tasks.domain.service.OrderItemService;
import com.tasks.domain.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/itens_order")
public class ItemOrderController {

    private final OrderItemService orderItemService;

    @Autowired
    public ItemOrderController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getItensOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.orderItemService.getOrderItensByOrderId(id));
    }

    @DeleteMapping(path = {"/{itemId}"})
    public ResponseEntity deleteItem(@PathVariable Integer itemId) {

        this.orderItemService.deleteByOrderItemId(itemId);

        return ResponseEntity.ok(new Response("Sucessful to delete item!", String.valueOf(HttpStatus.OK.value())));
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody OrderItemDTO orderItemDTO) throws NotFoundException {
        return ResponseEntity.ok(this.orderItemService.save(orderItemDTO));
    }

    @PutMapping
    public ResponseEntity put(@Valid @RequestBody OrderItemDTO orderItemDTO) throws NotFoundException {
        return ResponseEntity.ok(this.orderItemService.put(orderItemDTO));
    }
}
