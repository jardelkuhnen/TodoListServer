package com.tasks.security.service;

import com.tasks.domain.model.SolicitacaoToken;
import com.tasks.domain.repository.SolicitacaoTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoTokenService {

    private final SolicitacaoTokenRepository solicitacaoTokenRepository;

    @Autowired
    public SolicitacaoTokenService(SolicitacaoTokenRepository solicitacaoTokenRepository) {
        this.solicitacaoTokenRepository = solicitacaoTokenRepository;
    }

    public SolicitacaoToken save(SolicitacaoToken solicitacaoToken) {
        return this.solicitacaoTokenRepository.save(solicitacaoToken);
    }

    public SolicitacaoToken findByToken(String token) {
        return this.solicitacaoTokenRepository.findByToken(token);
    }

    public List<SolicitacaoToken> findByUserId(Long userId, Boolean isValid) {
        return this.solicitacaoTokenRepository.findByUserIdAndIsValid(userId, isValid);
    }

    public void saveAll(List<SolicitacaoToken> solicitacaoTokenList) {
        this.solicitacaoTokenRepository.saveAll(solicitacaoTokenList);
    }
}
