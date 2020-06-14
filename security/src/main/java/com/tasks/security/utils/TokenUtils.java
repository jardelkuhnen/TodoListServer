package com.tasks.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenUtils {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_ROLE = "role";
    static final String CLAIM_KEY_CREATED = "created";


}
