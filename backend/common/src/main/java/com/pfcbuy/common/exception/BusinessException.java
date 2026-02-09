package com.pfcbuy.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    
    private Integer code;
    private String message;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
