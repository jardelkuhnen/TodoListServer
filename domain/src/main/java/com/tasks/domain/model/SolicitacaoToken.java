package com.tasks.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitacao_token")
public class SolicitacaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column
    private String token;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column
    private Boolean isValid;

    @Column(nullable = false)
    private String tipoSolicitacao;


}
