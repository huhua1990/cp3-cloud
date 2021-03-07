package com.cp3.base.exception;

import com.cp3.base.exception.code.BaseExceptionCode;

/**
 * @Description 无权限异常
 * @Auther: huhua
 * @Date: 2021/2/2
 */
public class ForbiddenException extends BaseUncheckedException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(int code, String message) {
        super(code, message);
    }

    public static ForbiddenException wrap(BaseExceptionCode ex) {
        return new ForbiddenException(ex.getCode(), ex.getMsg());
    }

    @Override
    public String toString() {
        return "ForbiddenException [message=" + getMessage() + ", code=" + getCode() + "]";
    }

}
