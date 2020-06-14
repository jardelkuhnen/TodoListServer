package com.tasks.domain.exception;

import com.tasks.domain.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {

    public NotFoundException(Response response) {
        super(response.getMessage());
    }
}
