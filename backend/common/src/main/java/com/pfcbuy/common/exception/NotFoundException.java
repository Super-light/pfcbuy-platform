package com.pfcbuy.common.exception;

/**
 * 资源未找到异常
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public class NotFoundException extends BusinessException {
    
    public NotFoundException(String message) {
        super(404, message);
    }
}
