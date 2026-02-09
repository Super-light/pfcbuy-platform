package com.pfcbuy.common.exception;

/**
 * 参数验证异常
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public class ValidationException extends BusinessException {
    
    public ValidationException(String message) {
        super(400, message);
    }
}
