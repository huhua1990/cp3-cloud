package com.cp3.cloud.sms.enumeration;

import com.cp3.cloud.base.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

/**
 * <p>
 * 实体注释中生成的类型枚举
 * 短信发送状态
 * </p>
 *
 * @author cp3
 * @date 2019-08-01
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "SendStatus", description = "发送状态-枚举")
public enum SendStatus implements BaseEnum {

    /**
     * WAITING="等待发送"
     */
    WAITING("等待发送"),
    /**
     * SUCCESS="发送成功"
     */
    SUCCESS("发送成功"),
    /**
     * FAIL="发送失败"
     */
    FAIL("发送失败"),
    ;

    @ApiModelProperty(value = "描述")
    private String desc;


    public static SendStatus match(String val, SendStatus def) {
        return Stream.of(values()).parallel().filter((item) -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static SendStatus get(String val) {
        return match(val, null);
    }


    public boolean eq(SendStatus val) {
        return val == null ? false : eq(val.name());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "WAITING,SUCCESS,FAIL", example = "WAITING")
    public String getCode() {
        return this.name();
    }

}
