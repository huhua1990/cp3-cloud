package com.cp3.cloud.oauth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.cp3.base.basic.R;
import com.cp3.base.cache.model.CacheKey;
import com.cp3.base.cache.repository.CacheOps;
import com.cp3.base.exception.BizException;
import com.cp3.cloud.common.cache.common.CaptchaCacheKeyBuilder;
import com.cp3.cloud.oauth.properties.CaptchaProperties;
import com.cp3.cloud.oauth.service.ValidateCodeService;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cp3.base.exception.code.ExceptionCode.CAPTCHA_ERROR;

/**
 * 验证码服务
 *
 * @author zuihou
 */
@Service
@RequiredArgsConstructor
public class ValidateCodeServiceImpl implements ValidateCodeService {

    private final CacheOps cacheOps;
    private final CaptchaProperties captchaProperties;

    @Override
    public void create(String key, HttpServletResponse response) throws IOException {
        if (StrUtil.isBlank(key)) {
            throw BizException.validFail("验证码key不能为空");
        }
        //setHeader(response, "arithmetic");
        //Captcha captcha = createCaptcha("arithmetic");
        Captcha captcha = createCaptcha();

        CacheKey cacheKey = new CaptchaCacheKeyBuilder().key(key);
        cacheOps.set(cacheKey, StringUtils.lowerCase(captcha.text()));

        setHeader(response);
        captcha.out(response.getOutputStream());
    }

    @Override
    public R<Boolean> check(String key, String value) {
        if (StrUtil.isBlank(value)) {
            return R.fail(CAPTCHA_ERROR.build("请输入验证码"));
        }
        CacheKey cacheKey = new CaptchaCacheKeyBuilder().key(key);
        String code = cacheOps.get(cacheKey);
        if (StrUtil.isEmpty(code)) {
            return R.fail(CAPTCHA_ERROR.build("验证码已过期"));
        }
        if (!StringUtils.equalsIgnoreCase(value, code)) {
            return R.fail(CAPTCHA_ERROR.build("验证码不正确"));
        }
        cacheOps.del(cacheKey);
        return R.success(true);
    }

    @Deprecated
    private Captcha createCaptcha(String type) {
        Captcha captcha;
        if (StrUtil.equalsIgnoreCase(type, "gif")) {
            captcha = new GifCaptcha(115, 42, 4);
        } else if (StrUtil.equalsIgnoreCase(type, "png")) {
            captcha = new SpecCaptcha(115, 42, 4);
        } else if (StrUtil.equalsIgnoreCase(type, "chinese")) {
            captcha = new ChineseCaptcha(115, 42);
        } else  /*if (StrUtil.equalsIgnoreCase(type, "arithmetic")) */ {
            captcha = new ArithmeticCaptcha(115, 42);
        }
        captcha.setCharType(2);
        return captcha;
    }

    private Captcha createCaptcha() {
        Captcha captcha;
        CaptchaProperties.CaptchaType type = captchaProperties.getType();
        switch (type) {
            case GIF:
                captcha = new GifCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight(), captchaProperties.getLen());
                break;
            case SPEC:
                captcha = new SpecCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight(), captchaProperties.getLen());
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight(), captchaProperties.getLen());
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight(), captchaProperties.getLen());
                break;
            case ARITHMETIC:
            default:
                captcha = new ArithmeticCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight(), captchaProperties.getLen());
                break;
        }
        captcha.setCharType(captchaProperties.getCharType());

        return captcha;
    }

    @Deprecated
    private void setHeader(HttpServletResponse response, String type) {
        if (StrUtil.equalsIgnoreCase(type, "gif")) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }

    private void setHeader(HttpServletResponse response) {
        response.setContentType(captchaProperties.getType().getContentType());
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
