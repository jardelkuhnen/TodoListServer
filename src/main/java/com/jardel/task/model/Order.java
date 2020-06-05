package com.jardel.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    public Order(Integer id, String description, LocalDateTime registerDate, LocalDateTime updateDate) {
        this.id = id;
        this.description = description;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;

    @Column
    private LocalDateTime registerDate;

    @Column
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> itens;


}

