package com.cp3.cloud.boot.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.cp3.cloud.boot.undertow.UndertowServerFactoryCustomizer;
import com.cp3.cloud.converter.String2DateConverter;
import com.cp3.cloud.converter.String2LocalDateConverter;
import com.cp3.cloud.converter.String2LocalDateTimeConverter;
import com.cp3.cloud.converter.String2LocalTimeConverter;
import com.cp3.cloud.jackson.MyJacksonModule;
import com.cp3.cloud.utils.CodeGenerate;
import com.cp3.cloud.utils.SpringUtils;
import io.undertow.Undertow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.cp3.cloud.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

/**
 * 基础配置类
 *
 * @author cp3
 * @date 2019-06-22 22:53
 */
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public abstract class BaseConfig {

    /**
     * 全局配置 序列化和反序列化规则
     * addSerializer：序列化 （Controller 返回 给前端的json）
     * 1. Long -> string
     * 2. BigInteger -> string
     * 3. BigDecimal -> string
     * 4. date -> string
     * 5. LocalDateTime -> "yyyy-MM-dd HH:mm:ss"
     * 6. LocalDate -> "yyyy-MM-dd"
     * 7. LocalTime -> "HH:mm:ss"
     * 8. BaseEnum -> {"code": "xxx", "desc": "xxx"}
     *
     * <p>
     * addDeserializer: 反序列化 （前端调用接口时，传递到后台的json）
     * 1.  {"code": "xxx"} -> Enum
     * 2. "yyyy-MM-dd HH:mm:ss" -> LocalDateTime
     * 3. "yyyy-MM-dd" -> LocalDate
     * 4. "HH:mm:ss" -> LocalTime
     *
     * @param builder
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnClass(ObjectMapper.class)
    @ConditionalOnMissingBean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        objectMapper
                .setLocale(Locale.CHINA)
                //去掉默认的时间戳格式
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 时区
                .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                //Date参数日期格式
                .setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale.CHINA))

                //该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
                // 忽略不能转移的字符
                .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true)
                .findAndRegisterModules()

                //在使用spring boot + jpa/hibernate，如果实体字段上加有FetchType.LAZY，并使用jackson序列化为json串时，会遇到SerializationFeature.FAIL_ON_EMPTY_BEANS异常
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //忽略未知字段
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //单引号处理
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
        ;

        //反序列化时，属性不存在的兼容处理
        objectMapper
                .getDeserializationConfig()
                .withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        objectMapper.registerModule(new MyJacksonModule());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    /**
     * 解决 @RequestParam(value = "date") Date date
     * date 类型参数 格式问题
     *
     * @return
     */
    @Bean
    public Converter<String, Date> dateConvert() {
        return new String2DateConverter();
    }

    /**
     * 解决 @RequestParam(value = "time") LocalDate time
     *
     * @return
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new String2LocalDateConverter();
    }

    /**
     * 解决 @RequestParam(value = "time") LocalTime time
     *
     * @return
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new String2LocalTimeConverter();
    }

    /**
     * 解决 @RequestParam(value = "time") LocalDateTime time
     *
     * @return
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new String2LocalDateTimeConverter();
    }

    //---------------------------------------序列化配置end----------------------------------------------

    /**
     * 长度都是8位的字符串
     *
     * @param machineCode
     * @return
     */
    @Bean("codeGenerate")
    public CodeGenerate codeGenerate(@Value("${id-generator.machine-code:1}") Long machineCode) {
        return new CodeGenerate(machineCode.intValue());
    }

    /**
     * Spring 工具类
     *
     * @param applicationContext
     * @return
     */
    @Bean
    public SpringUtils getSpringUtils(ApplicationContext applicationContext) {
        SpringUtils.setApplicationContext(applicationContext);
        return new SpringUtils();
    }


    /**
     * gateway 网关模块需要禁用 spring-webmvc 相关配置，必须通过在类上面加限制条件方式来实现， 不能直接Bean上面加
     */
    @ConditionalOnProperty(prefix = "zuihou.webmvc", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class WebMvcConfig {
        @Bean
        //@ConditionalOnProperty(prefix = "zuihou.webmvc", name = "enabled", havingValue = "true", matchIfMissing = true)
        @ConditionalOnClass(Undertow.class)
        public UndertowServerFactoryCustomizer getUndertowServerFactoryCustomizer() {
            return new UndertowServerFactoryCustomizer();
        }
    }

    @Bean
    @ConditionalOnClass
    public GlobalMvcConfigurer getGlobalMvcConfigurer() {
        return new GlobalMvcConfigurer();
    }
}
