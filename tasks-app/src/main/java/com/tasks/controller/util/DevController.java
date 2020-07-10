package com.tasks.controller.util;

import com.tasks.emailsender.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev-controller")
public class DevController {

    @Autowired
    private final SendEmailService sendEmailService;

    @Autowired
    public DevController(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody String email) {

        this.sendEmailService.sendEmail(email, "Email teste", "Aqui jaix um email de teste manolo");

        return ResponseEntity.ok().build();
    }

}
