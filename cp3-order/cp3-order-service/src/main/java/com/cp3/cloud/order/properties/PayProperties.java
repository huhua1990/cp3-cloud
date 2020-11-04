package com.cp3.cloud.order.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 98050
 * @create: 2018-10-27 11:38
 **/
//@ConfigurationProperties(prefix = "cp3.pay")
@Configuration
@RefreshScope
public class PayProperties {

    /**
     * 公众账号ID
     */
    @Value("${cp3.pay.appId}")
    private String appId;

    /**
     * 商户号
     */
    @Value("${cp3.pay.mchId}")
    private String mchId;

    /**
     * 生成签名的密钥
     */
    @Value("${cp3.pay.key}")
    private String key;

    /**
     * 连接超时时间
     */
    @Value("${cp3.pay.connectTimeoutMs}")
    private int connectTimeoutMs;

    /**
     * 读取超时时间
     */
    @Value("${cp3.pay.connectTimeoutMs}")
    private int readTimeoutMs;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }
}
