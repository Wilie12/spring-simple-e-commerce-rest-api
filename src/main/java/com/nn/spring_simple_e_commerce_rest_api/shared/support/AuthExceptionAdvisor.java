package com.nn.spring_simple_e_commerce_rest_api.shared.support;

import com.nn.spring_simple_e_commerce_rest_api.shared.api.response.ErrorMessageResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@ControllerAdvice
public class AuthExceptionAdvisor {
    public static final Logger LOG = LoggerFactory.getLogger(AuthExceptionAdvisor.class);

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessageResponse badCredentials(Exception e) {
        LOG.error(e.getMessage(), e);
        return new ErrorMessageResponse("The username or password is incorrect");
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessageResponse accountLocked(Exception e) {
        LOG.error(e.getMessage(), e);
        return new ErrorMessageResponse("The account is locked");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessageResponse accessDenied(Exception e) {
        LOG.error(e.getMessage(), e);
        return new ErrorMessageResponse("You are not authorized to access this resource");
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessageResponse invalidTokenSignature(Exception e) {
        LOG.error(e.getMessage(), e);
        return new ErrorMessageResponse("The JWT signature is invalid");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessageResponse expiredToken(Exception e) {
        LOG.error(e.getMessage(), e);
        return new ErrorMessageResponse("The JWT token has expired");
    }
}
