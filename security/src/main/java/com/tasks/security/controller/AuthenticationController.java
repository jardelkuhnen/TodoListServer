package com.tasks.security.controller;

import com.tasks.domain.exception.NotFoundException;
import com.tasks.security.dto.*;
import com.tasks.security.service.JwtAuthenticationService;
import com.tasks.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    private final UserService userService;

    @Autowired
    public AuthenticationController(JwtAuthenticationService jwtAuthenticationService,
                                    UserService userService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> generateToken(@Valid @RequestBody JwtAuthenticatoinDTO jwtAuthenticatoinDTO) throws Exception {

        log.info("Gerando token para o email {}.", jwtAuthenticatoinDTO.getEmail());

        UserDTO userDTO = this.jwtAuthenticationService.generateToken(jwtAuthenticatoinDTO);


        return ResponseEntity.ok(userDTO);
    }

    /**
     * Refresh the token
     *
     * @param request
     * @return
     * @throws NotFoundException
     */
    @PostMapping(value = "/refresh")
    public ResponseEntity<UserDTO> gerarRefreshTokenJwt(@RequestBody RefreshTokenDTO refreshTokenDTO, HttpServletRequest request) throws NotFoundException {

        UserDTO userDTO = this.jwtAuthenticationService.refreshToken(request.getHeader(TOKEN_HEADER), refreshTokenDTO.getEmail());

        return ResponseEntity.ok(userDTO);
    }

    /**
     * Register new user
     *
     * @param registerUserDTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterUserDTO registerUserDTO) throws Exception {

        UserDTO userDTO = this.userService.registerUser(registerUserDTO);

        return ResponseEntity.ok(userDTO);
    }

    /**
     * Reset password of user registered
     *
     * @param resetPasswordDTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/reset")
    public ResponseEntity<UserDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws Exception {

        UserDTO userDTO = this.userService.resetPassword(resetPasswordDTO);

        return ResponseEntity.ok(userDTO);
    }

}
