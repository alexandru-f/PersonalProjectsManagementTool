package io.alexandru.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(String exception) {
        super(exception);
    }
}
