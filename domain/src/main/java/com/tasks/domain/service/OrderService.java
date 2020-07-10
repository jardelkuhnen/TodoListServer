package com.tasks.domain.service;

import com.tasks.domain.dto.OrderDTO;
import com.tasks.domain.dto.UserLogged;
import com.tasks.domain.enums.RoleAccess;
import com.tasks.domain.exception.NotFoundException;
import com.tasks.domain.model.Order;
import com.tasks.domain.model.OrderItem;
import com.tasks.domain.repository.OrderRepository;
import com.tasks.domain.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final SystemUtilsService systemUtilsService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderItemService orderItemService,
                        SystemUtilsService systemUtilsService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.systemUtilsService = systemUtilsService;
    }

    @Transactional
    public OrderDTO save(OrderDTO orderDTO) {

        if (orderDTO.getId() != null) {
            this.orderItemService.delete(orderDTO.getId());
        }

        Order order = OrderDTO.of(orderDTO);

        order.setRegisterDate(LocalDateTime.now());
        order.setUserId(this.systemUtilsService.getUserLogged().getId());

        order = this.orderRepository.save(order);

        List<OrderItem> itens = this.orderItemService.save(orderDTO.getItens(), order);
        order.setItens(itens);

        return OrderDTO.of(order);
    }

    public Page<OrderDTO> getAll(Pageable pageable) {

        UserLogged userLogged = this.systemUtilsService.getUserLogged();

        if(RoleAccess.ROLE_ADMIN == userLogged.getRole()) {
            return this.orderRepository.findAll(pageable).map(OrderDTO::of);
        }

        return this.orderRepository.findByUserId(userLogged.getId(), pageable);
    }

    @Transactional
    public OrderDTO put(OrderDTO orderDTO) throws NotFoundException {

        Optional<Order> orderOptional = this.orderRepository.findById(orderDTO.getId());

        if (!orderOptional.isPresent()) {
            throw new NotFoundException(new Response("Order not found!", String.valueOf(HttpStatus.NOT_FOUND.value())));
        }

        List<OrderItem> itens = this.orderItemService.put(orderDTO.getItens(), orderDTO.getId());

        Order order = orderOptional.get();

        order.setDescription(orderDTO.getDescription());
        order.setRegisterDate(orderDTO.getRegisterDate());
        order.setUpdateDate(LocalDateTime.now());
        order.setItens(itens);

        return OrderDTO.of(order);
    }

    @Transactional
    public void delete(Integer orderId) throws NotFoundException {

        Optional<Order> orderOptional = this.orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            throw new NotFoundException(new Response("Order not found!", String.valueOf(HttpStatus.NOT_FOUND.value())));
        }

        Order order = orderOptional.get();

        this.orderItemService.delete(orderId);

        this.orderRepository.delete(order);
    }

    public OrderDTO getById(Integer orderId) throws NotFoundException {

        UserLogged userLogged = this.systemUtilsService.getUserLogged();

        Order order;

        if(RoleAccess.ROLE_ADMIN.equals(userLogged.getRole())) {
            order = this.orderRepository.findById(orderId).get();
        } else {
            order = this.orderRepository.findByIdAndUserId(orderId, userLogged.getId());
        }

        if (order == null) {
            throw new NotFoundException(new Response("Order not found!", String.valueOf(HttpStatus.NOT_FOUND.value())));
        }


        return OrderDTO.of(order);
    }

    public String convertBinToDec(String binario) throws Exception {

        if (binario.length() > 8) {
            throw new Exception("Input inválido. Informe apenas 8 caracters!");
        }

        String regex = "[^\0-1]";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(binario);

        while (matcher.find()) {
            throw new Exception("Dado informdo contém caracteres não validos: " + matcher.start());
        }

        Integer decimal = Integer.parseInt(binario, 2);

        return decimal.toString();

    }
}
