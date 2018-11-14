package com.walle.springdemo.exception;

import com.walle.springdemo.result.Result;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalException {
//
//    @ExceptionHandler(value = Exception.class)
//    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
//        if (e instanceof BindException) {
//            BindException bindException = (BindException) e;
//            String message = bindException.getMessage();
//            return Result.error(message);
//        }
//    }
}
