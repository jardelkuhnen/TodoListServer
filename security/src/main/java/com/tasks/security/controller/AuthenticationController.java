package com.tasks.security.controller;

import com.tasks.domain.exception.NotFoundException;
import com.tasks.security.dto.JwtAuthenticatoinDTO;
import com.tasks.security.dto.RefreshTokenDTO;
import com.tasks.security.dto.UserDTO;
import com.tasks.security.service.JwtAuthenticationService;
import com.tasks.security.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtAuthenticationService jwtAuthenticationService;

    @Autowired
    public AuthenticationController(JwtAuthenticationService jwtAuthenticationService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<Response<UserDTO>> generateToken(@Valid @RequestBody JwtAuthenticatoinDTO jwtAuthenticatoinDTO) throws Exception {

        log.info("Gerando token para o email {}.", jwtAuthenticatoinDTO.getEmail());

        UserDTO userDTO = this.jwtAuthenticationService.generateToken(jwtAuthenticatoinDTO);

        Response response = new Response();
        response.setData(userDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * Refresh the token
     *
     * @param request
     * @return
     * @throws NotFoundException
     */
    @PostMapping(value = "/refresh")
    public ResponseEntity<Response<UserDTO>> gerarRefreshTokenJwt(@RequestBody RefreshTokenDTO refreshTokenDTO, HttpServletRequest request) throws NotFoundException {

        Response<UserDTO> response = new Response<UserDTO>();

        UserDTO userDTO = this.jwtAuthenticationService.refreshToken(request.getHeader(TOKEN_HEADER), refreshTokenDTO.getEmail());
        response.setData(userDTO);

        return ResponseEntity.ok(response);
    }

}
