package com.rz.lease.common.exception;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rz.lease.common.result.Result;

@ControllerAdvice
public class GlobalException {

    private static final Logger log = Logger.getLogger(GlobalException.class.getName());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handle(Exception e) {
        // if
        // ("org.springframework.web.servlet.resource.NoResourceFoundException".equals(e.getClass().getName()))
        // {
        // return ResponseEntity.notFound().build();
        // }

        // log.log(Level.SEVERE, "Unhandled application exception", e);
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(LeaseException.class)
    @ResponseBody
    public Object handle(LeaseException e) {
        e.printStackTrace();

        return Result.fail(e.getCode(), e.getMessage());
    }

}
