package com.ldx.blog.config;

import cn.dev33.satoken.exception.NotLoginException;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * @author Uaena
 * @date 2023/5/26 21:34
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {
    /**
     * 默认全局异常处理。
     *
     * @param e the e
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    public Result<ResultCodeEnum> exception(Exception e) {
        if (e instanceof NotLoginException) {
            return Result.fail(ResultCodeEnum.NOT_LOGIN);
        }
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return Result.fail(ResultCodeEnum.FAIL);
    }

    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Result<String>> handleValidatedException(Exception e) {
        Result<String> resp = null;

        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            resp = Result.fail(ResultCodeEnum.FAIL.getCode(),
                    ex.getBindingResult().getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; "))
            );
        } else if (e instanceof ConstraintViolationException) {
            // BeanValidation GET simple param
            ConstraintViolationException ex = (ConstraintViolationException) e;
            resp = Result.fail(ResultCodeEnum.FAIL.getCode(),
                    ex.getConstraintViolations().stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("; "))
            );
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            BindException ex = (BindException) e;
            resp = Result.fail(ResultCodeEnum.FAIL.getCode(),
                    ex.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; "))
            );
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


}
