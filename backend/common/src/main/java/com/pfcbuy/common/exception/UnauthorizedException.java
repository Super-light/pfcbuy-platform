package com.pfcbuy.common.exception;

/**
 * 认证异常
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public class UnauthorizedException extends BusinessException {
    
    public UnauthorizedException(String message) {
        super(401, message);
    }
    
    public UnauthorizedException() {
        super(401, "未授权访问");
    }
}
