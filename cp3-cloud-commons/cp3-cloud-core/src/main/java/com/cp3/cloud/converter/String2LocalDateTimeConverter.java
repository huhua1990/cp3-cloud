package com.cp3.cloud.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.cp3.cloud.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;
import static com.cp3.cloud.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT_EN;
import static com.cp3.cloud.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT_EN_MATCHES;
import static com.cp3.cloud.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT_MATCHES;
import static com.cp3.cloud.utils.DateUtils.SLASH_DATE_TIME_FORMAT;
import static com.cp3.cloud.utils.DateUtils.SLASH_DATE_TIME_FORMAT_MATCHES;

/**
 * 解决入参为 Date类型
 *
 * @author cp3
 * @date 2019-04-30
 */
public class String2LocalDateTimeConverter extends BaseDateConverter<LocalDateTime> implements Converter<String, LocalDateTime> {

    protected static final Map<String, String> FORMAT = new LinkedHashMap(2);

    static {
        FORMAT.put(DEFAULT_DATE_TIME_FORMAT, DEFAULT_DATE_TIME_FORMAT_MATCHES);
        FORMAT.put(SLASH_DATE_TIME_FORMAT, SLASH_DATE_TIME_FORMAT_MATCHES);
        FORMAT.put(DEFAULT_DATE_TIME_FORMAT_EN, DEFAULT_DATE_TIME_FORMAT_EN_MATCHES);
    }

    @Override
    protected Map<String, String> getFormat() {
        return FORMAT;
    }

    @Override
    public LocalDateTime convert(String source) {
        return super.convert(source, (key) -> LocalDateTime.parse(source, DateTimeFormatter.ofPattern(key)));
    }
}
