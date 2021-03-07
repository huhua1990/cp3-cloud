package com.cp3.base.exception;

import com.cp3.base.exception.code.ExceptionCode;

/**
 * @Description 无权限异常
 * @Auther: huhua
 * @Date: 2021/2/2
 */
public class UnauthorizedException extends BaseUncheckedException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(int code, String message) {
        super(code, message);
    }

    public static UnauthorizedException wrap(String msg) {
        return new UnauthorizedException(ExceptionCode.FORBIDDEN.getCode(), msg);
    }

    @Override
    public String toString() {
        return "UnauthorizedException [message=" + getMessage() + ", code=" + getCode() + "]";
    }

}
