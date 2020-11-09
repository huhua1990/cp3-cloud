package com.cp3.cloud.boot.handler;

import cn.hutool.core.util.StrUtil;
import com.cp3.cloud.base.R;
import com.cp3.cloud.exception.BizException;
import com.cp3.cloud.exception.code.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cp3.cloud.exception.code.ExceptionCode.METHOD_NOT_ALLOWED;
import static com.cp3.cloud.exception.code.ExceptionCode.REQUIRED_FILE_PARAM_EX;


/**
 * 全局异常统一处理
 *
 * @author cp3
 * @createTime 2017-12-13 17:04
 */
@Slf4j
public abstract class DefaultGlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R bizException(BizException ex, HttpServletRequest request) {
        log.warn("BizException:", ex);
        return R.result(ex.getCode(), null, ex.getMessage()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R httpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("HttpMessageNotReadableException:", ex);
        String message = ex.getMessage();
        if (StrUtil.containsAny(message, "Could not read document:")) {
            String msg = String.format("无法正确的解析json类型的参数：%s", StrUtil.subBetween(message, "Could not read document:", " at "));
            return R.result(ExceptionCode.PARAM_EX.getCode(), null, msg).setPath(request.getRequestURI());
        }
        return R.result(ExceptionCode.PARAM_EX.getCode(), null, ExceptionCode.PARAM_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R bindException(BindException ex, HttpServletRequest request) {
        log.warn("BindException:", ex);
        try {
            String msgs = ex.getBindingResult().getFieldError().getDefaultMessage();
            if (StrUtil.isNotEmpty(msgs)) {
                return R.result(ExceptionCode.PARAM_EX.getCode(), null, msgs).setPath(request.getRequestURI());
            }
        } catch (Exception ee) {
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = ex.getFieldErrors();
        fieldErrors.forEach((oe) ->
                msg.append("参数:[").append(oe.getObjectName())
                        .append(".").append(oe.getField())
                        .append("]的传入值:[").append(oe.getRejectedValue()).append("]与预期的字段类型不匹配.")
        );
        return R.result(ExceptionCode.PARAM_EX.getCode(), null, msg.toString()).setPath(request.getRequestURI());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("MethodArgumentTypeMismatchException:", ex);
        MethodArgumentTypeMismatchException eee = (MethodArgumentTypeMismatchException) ex;
        StringBuilder msg = new StringBuilder("参数：[").append(eee.getName())
                .append("]的传入值：[").append(eee.getValue())
                .append("]与预期的字段类型：[").append(eee.getRequiredType().getName()).append("]不匹配");
        return R.result(ExceptionCode.PARAM_EX.getCode(), null, msg.toString()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R illegalStateException(IllegalStateException ex, HttpServletRequest request) {
        log.warn("IllegalStateException:", ex);
        return R.result(ExceptionCode.ILLEGALA_ARGUMENT_EX.getCode(), null, ExceptionCode.ILLEGALA_ARGUMENT_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R missingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("MissingServletRequestParameterException:", ex);
        StringBuilder msg = new StringBuilder();
        msg.append("缺少必须的[").append(ex.getParameterType()).append("]类型的参数[").append(ex.getParameterName()).append("]");
        return R.result(ExceptionCode.ILLEGALA_ARGUMENT_EX.getCode(), null, msg.toString()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R nullPointerException(NullPointerException ex, HttpServletRequest request) {
        log.warn("NullPointerException:", ex);
        return R.result(ExceptionCode.NULL_POINT_EX.getCode(), null, ExceptionCode.NULL_POINT_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R illegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("IllegalArgumentException:", ex);
        return R.result(ExceptionCode.ILLEGALA_ARGUMENT_EX.getCode(), null, ExceptionCode.ILLEGALA_ARGUMENT_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        log.warn("HttpMediaTypeNotSupportedException:", ex);
        MediaType contentType = ex.getContentType();
        if (contentType != null) {
            StringBuilder msg = new StringBuilder();
            msg.append("请求类型(Content-Type)[").append(contentType.toString()).append("] 与实际接口的请求类型不匹配");
            return R.result(ExceptionCode.MEDIA_TYPE_EX.getCode(), null, msg.toString()).setPath(request.getRequestURI());
        }
        return R.result(ExceptionCode.MEDIA_TYPE_EX.getCode(), null, "无效的Content-Type类型").setPath(request.getRequestURI());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R missingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request) {
        log.warn("MissingServletRequestPartException:", ex);
        return R.result(REQUIRED_FILE_PARAM_EX.getCode(), null, REQUIRED_FILE_PARAM_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R servletException(ServletException ex, HttpServletRequest request) {
        log.warn("ServletException:", ex);
        String msg = "UT010016: Not a multi part request";
        if (msg.equalsIgnoreCase(ex.getMessage())) {
            return R.result(REQUIRED_FILE_PARAM_EX.getCode(), null, REQUIRED_FILE_PARAM_EX.getMsg());
        }
        return R.result(ExceptionCode.SYSTEM_BUSY.getCode(), null, ex.getMessage()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R multipartException(MultipartException ex, HttpServletRequest request) {
        log.warn("MultipartException:", ex);
        return R.result(REQUIRED_FILE_PARAM_EX.getCode(), null, REQUIRED_FILE_PARAM_EX.getMsg()).setPath(request.getRequestURI());
    }

    /**
     * jsr 规范中的验证异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R constraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("ConstraintViolationException:", ex);
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));

        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message2 = String.format("%s:%s", path, violation.getMessage());

        return R.result(ExceptionCode.BASE_VALID_PARAM.getCode(), null, message).setPath(request.getRequestURI());
    }

    /**
     * spring 封装的参数验证异常， 在conttoller中没有写result参数时，会进入
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException:", ex);
        return R.result(ExceptionCode.BASE_VALID_PARAM.getCode(), null, ex.getBindingResult().getFieldError().getDefaultMessage()).setPath(request.getRequestURI());
    }

    /**
     * 其他异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R otherExceptionHandler(Exception ex, HttpServletRequest request) {
        log.warn("Exception:", ex);
        if (ex.getCause() instanceof BizException) {
            return this.bizException((BizException) ex.getCause(), request);
        }
        return R.result(ExceptionCode.SYSTEM_BUSY.getCode(), null, ExceptionCode.SYSTEM_BUSY.getMsg()).setPath(request.getRequestURI());
    }


    /**
     * 返回状态码:405
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("HttpRequestMethodNotSupportedException:", ex);
        return R.result(METHOD_NOT_ALLOWED.getCode(), null, METHOD_NOT_ALLOWED.getMsg()).setPath(request.getRequestURI());
    }


    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R persistenceException(PersistenceException ex, HttpServletRequest request) {
        log.warn("PersistenceException:", ex);
        if (ex.getCause() instanceof BizException) {
            BizException cause = (BizException) ex.getCause();
            return R.result(cause.getCode(), null, cause.getMessage());
        }
        return R.result(ExceptionCode.SQL_EX.getCode(), null, ExceptionCode.SQL_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(MyBatisSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R myBatisSystemException(MyBatisSystemException ex, HttpServletRequest request) {
        log.warn("PersistenceException:", ex);
        if (ex.getCause() instanceof PersistenceException) {
            return this.persistenceException((PersistenceException) ex.getCause(), request);
        }
        return R.result(ExceptionCode.SQL_EX.getCode(), null, ExceptionCode.SQL_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R sqlException(SQLException ex, HttpServletRequest request) {
        log.warn("SQLException:", ex);
        return R.result(ExceptionCode.SQL_EX.getCode(), null, ExceptionCode.SQL_EX.getMsg()).setPath(request.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("DataIntegrityViolationException:", ex);
        return R.result(ExceptionCode.SQL_EX.getCode(), null, ExceptionCode.SQL_EX.getMsg()).setPath(request.getRequestURI());
    }

}
