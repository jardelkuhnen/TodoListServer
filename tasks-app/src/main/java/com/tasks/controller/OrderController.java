package com.tasks.controller;


import com.tasks.domain.dto.OrderDTO;
import com.tasks.domain.exception.NotFoundException;
import com.tasks.domain.service.OrderService;
import com.tasks.domain.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity save(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(this.orderService.save(orderDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.ok(this.orderService.getById(id));
    }

    @GetMapping
    public ResponseEntity getAll(Pageable pageable) {
        return ResponseEntity.ok(this.orderService.getAll(pageable));
    }

    @PutMapping
    public ResponseEntity put(@Valid @RequestBody OrderDTO orderDTO) throws NotFoundException {
        return ResponseEntity.ok(this.orderService.put(orderDTO));
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity delete(@PathVariable Integer id) throws NotFoundException {
        this.orderService.delete(id);

        return ResponseEntity.ok(new Response("Sucessful to delete order!", String.valueOf(HttpStatus.OK.value())));

    }

    @PostMapping("bin-to-dec/{binario}")
    public ResponseEntity transforBin2Decimal(@PathVariable @NotBlank String binario) throws Exception {
        return ResponseEntity.ok(this.orderService.convertBinToDec(binario));
    }

}
