package com.cp3.cloud.exception;

/**
 * 非运行期异常基类，所有自定义非运行时异常继承该类
 *
 * @author cp3
 * @version 1.0,
 * @see RuntimeException
 */
public class BaseUncheckedException extends RuntimeException implements BaseException {

    private static final long serialVersionUID = -778887391066124051L;

    /**
     * 异常信息
     */
    protected String message;

    /**
     * 具体异常码
     */
    protected int code;

    public BaseUncheckedException(Throwable cause) {
        super(cause);
    }

    public BaseUncheckedException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }


    public BaseUncheckedException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseUncheckedException(int code, String format, Object... args) {
        super(String.format(format, args));
        this.code = code;
        this.message = String.format(format, args);
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
}
