package com.tasks.domain.repository;

import com.tasks.domain.model.SolicitacaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoTokenRepository extends JpaRepository<SolicitacaoToken, Long> {

    SolicitacaoToken findByToken(String token);

    List<SolicitacaoToken> findByUserIdAndIsValid(Long userId, Boolean isValid);
}
