package com.tasks.security.service;


import com.tasks.domain.dto.UserDTO;
import com.tasks.domain.dto.UserLogged;
import com.tasks.domain.enums.RoleAccess;
import com.tasks.domain.exception.NotFoundException;
import com.tasks.domain.model.SolicitacaoToken;
import com.tasks.domain.model.User;
import com.tasks.domain.model.UserChangePassword;
import com.tasks.domain.property.SystemProperties;
import com.tasks.domain.repository.UserRepository;
import com.tasks.domain.service.SystemUtilsService;
import com.tasks.domain.service.UserChangePasswordService;
import com.tasks.domain.util.DateUtils;
import com.tasks.emailsender.service.SendEmailService;
import com.tasks.security.dto.RegisterUserDTO;
import com.tasks.security.dto.ResetPasswordDTO;
import com.tasks.security.enums.TipoSolicitacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private static final Integer EXPIRATION_DATE = 1;

    private final UserRepository userRepository;
    private final SystemUtilsService systemUtilsService;
    private final SystemProperties systemProperties;
    private final SendEmailService sendEmailService;
    private final UserChangePasswordService userChangePasswordService;
    private final SolicitacaoTokenService solicitacaoTokenService;
    private final DateUtils dateUtils;

    @Autowired
    public UserService(UserRepository userRepository, SystemUtilsService systemUtilsService,
                       SystemProperties systemProperties, SendEmailService sendEmailService,
                       UserChangePasswordService userChangePasswordService,
                       SolicitacaoTokenService solicitacaoTokenService, DateUtils dateUtils) {
        this.userRepository = userRepository;
        this.systemUtilsService = systemUtilsService;
        this.systemProperties = systemProperties;
        this.sendEmailService = sendEmailService;
        this.userChangePasswordService = userChangePasswordService;
        this.solicitacaoTokenService = solicitacaoTokenService;
        this.dateUtils = dateUtils;
    }


    public User loadUserByEmail(String userName) {
        User user = this.userRepository.findByEmail(userName);

        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("Email not found!");
    }

    @Transactional
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) throws Exception {

        //validateUseCanSaveADM(registerUserDTO);

        this.validateEmailRegistered(registerUserDTO);

        User user = RegisterUserDTO.of(registerUserDTO);
        user.setIsActive(false);
        user.setRegisterDate(LocalDateTime.now());

        user = this.userRepository.save(user);

        this.validExistsSolicitacaoToken(user.getId());

        SolicitacaoToken solicitationToken = this.saveSolicitationToken(user);

        this.sendConfirmEmailToken(solicitationToken.getToken(), solicitationToken.getExpirationDate(), user.getEmail());

//        this.sendEmailInformation(user);

        return UserDTO.of(user);
    }

    private void validExistsSolicitacaoToken(Long userId) {

        Boolean isValid = Boolean.TRUE;

        List<SolicitacaoToken> solicitacaoTokenList = this.solicitacaoTokenService.findByUserId(userId, isValid);

        if(solicitacaoTokenList == null || solicitacaoTokenList.isEmpty()) {
           return;
        }

        solicitacaoTokenList.stream().forEach(solicit -> solicit.setIsValid(false));

        this.solicitacaoTokenService.saveAll(solicitacaoTokenList);
    }

    private void sendConfirmEmailToken(String token, LocalDateTime expirationDate, String email) {
        String link = systemProperties.getUrlsystem() + "/auth/register/confirm?token=" + token;

        String subject = "Confirm to register";
        String text = "Access the link -> " + link + " to confirm your registration \n\n " +
                "This confirmation is valid until " + DateUtils.formatDDMMYYYHHMMSS(expirationDate);

        this.sendEmailService.sendEmail(email, subject, text);

    }

    private SolicitacaoToken saveSolicitationToken(User user) {

        SolicitacaoToken solicitacaoToken = new SolicitacaoToken();

        solicitacaoToken.setIsValid(true);
        solicitacaoToken.setUserId(user.getId());
        solicitacaoToken.setKey(user.getEmail());
        solicitacaoToken.setToken(UUID.randomUUID().toString());
        solicitacaoToken.setTipoSolicitacao(TipoSolicitacao.REGISTER.name());
        solicitacaoToken.setExpirationDate(LocalDateTime.now().plusHours(EXPIRATION_DATE));

        solicitacaoToken = this.solicitacaoTokenService.save(solicitacaoToken);

        return solicitacaoToken;
    }

    private void sendEmailInformation(User user) {

        String subject = "Hello World TaskApplication";
        String text = "Congratulation. You are member of our family. /n/n" +
                "Access out app using this webLink: " + systemProperties.getUrlsystem();

        this.sendEmailService.sendEmail(user.getEmail(), subject, text);

    }

    private void validateUseCanSaveADM(RegisterUserDTO registerUserDTO) {

        UserLogged userLogged = this.systemUtilsService.getUserLogged();

        if (RoleAccess.ROLE_ADMIN != userLogged.getRole()) {

            if (registerUserDTO.getRole().equalsIgnoreCase(RoleAccess.ROLE_ADMIN.getDescription())) {
                throw new NotFoundException("Just ADMIN users can register new Admin User.");
            }

        }

    }

    private void validateEmailRegistered(RegisterUserDTO registerUserDTO) throws Exception {
        User existsUser = this.userRepository.findByEmail(registerUserDTO.getEmail());

        if (existsUser != null && existsUser.getIsValidated()) {
            throw new Exception("You cannot register this email in this moment. Call to system administrator!");
        }
    }

    @Transactional
    public UserDTO resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {

        this.checkConfirmPassword(resetPasswordDTO);

        User user = this.userRepository.findByEmail(resetPasswordDTO.getEmail());

        if (Objects.isNull(user)) {
            throw new NotFoundException("User not found!");
        }

        this.registerChangePassword(user.getId(), user.getPassword());

        user.setPassword(new BCryptPasswordEncoder().encode(resetPasswordDTO.getPassword()));

        this.userRepository.save(user);

        return UserDTO.of(user);
    }

    private void registerChangePassword(Long userId, String oldPassword) {

        UserChangePassword userChangePassword = new UserChangePassword(userId, oldPassword);
        this.userChangePasswordService.save(userChangePassword);

    }

    private void checkConfirmPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {

        if (!resetPasswordDTO.getPassword().equalsIgnoreCase(resetPasswordDTO.getConfirmPassword())) {
            throw new Exception("Passwords are not equal!");
        }

    }

    public void registerUserLoged(String username) {

        User user = this.userRepository.findByEmail(username);

        if (user == null) {
            throw new NotFoundException("User not found wiht email: " + username);
        }

        System.setProperty("userLoggedName", user.getEmail());
        System.setProperty("userLoggedId", user.getId().toString());

    }

    @Transactional
    public void validateToken(String token) {

        log.info("Validating token -> {}", token);

        SolicitacaoToken solicitacaoToken = this.solicitacaoTokenService.findByToken(token);
        LocalDateTime now = LocalDateTime.now();

        if (!solicitacaoToken.getIsValid()) {
            log.info("Token is not valid!");
            return;
        }

        if (!now.isBefore(solicitacaoToken.getExpirationDate())) {
            log.info("Token is expired!");
            return;
        }


        solicitacaoToken.setIsValid(false);
        this.solicitacaoTokenService.save(solicitacaoToken);

        Optional<User> optionalUser = this.userRepository.findById(solicitacaoToken.getUserId());
        if (!optionalUser.isPresent()) {
            log.info("Não encontrado usuário com id -> {}", solicitacaoToken.getUserId());
            return;
        }

        User user = optionalUser.get();
        user.setIsActive(true);
        user.setIsValidated(true);
        this.userRepository.save(user);

        log.info("Success validation token!");

        this.sendEmailInformation(user);
    }
}
