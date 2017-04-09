package cane.brothers.rpc.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by cane on 06.04.17.
 */
@ControllerAdvice
public class RpcExceptionHandler {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(BadCredentialsException.class)
    public void handleUnauthorized(){
        log.info("bad credentials");
    }
}
