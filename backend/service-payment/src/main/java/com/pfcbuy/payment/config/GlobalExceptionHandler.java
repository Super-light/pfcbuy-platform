package com.pfcbuy.payment.config;

import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.common.exception.NotFoundException;
import com.pfcbuy.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 资源未找到异常
     */
    @ExceptionHandler(NotFoundException.class)
    public Result<Void> handleNotFoundException(NotFoundException e) {
        log.error("资源未找到: {}", e.getMessage());
        return Result.error(404, e.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null 
                ? e.getBindingResult().getFieldError().getDefaultMessage() 
                : "参数校验失败";
        log.error("参数校验失败: {}", message);
        return Result.error(400, message);
    }

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.error(500, "系统异常: " + e.getMessage());
    }
}
