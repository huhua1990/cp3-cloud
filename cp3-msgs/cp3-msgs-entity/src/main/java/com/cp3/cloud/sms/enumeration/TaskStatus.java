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
 * 发送任务
 * 所有的短息发送调用，都视为是一次短信任务，任务表只保存数据和执行状态等信息，
 * 具体的发送状态查看发送状态（#sms_send_status）表
 * </p>
 *
 * @author cp3
 * @date 2019-11-22
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TaskStatus", description = "执行状态-枚举")
public enum TaskStatus implements BaseEnum {

    /**
     * WAITING="等待执行"
     */
    WAITING("等待执行"),
    /**
     * SUCCESS="执行成功"
     */
    SUCCESS("执行成功"),
    /**
     * FAIL="执行失败"
     */
    FAIL("执行失败"),
    ;

    @ApiModelProperty(value = "描述")
    private String desc;


    public static TaskStatus match(String val, TaskStatus def) {
        return Stream.of(values()).parallel().filter((item) -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static TaskStatus get(String val) {
        return match(val, null);
    }

    public boolean eq(TaskStatus val) {
        return val == null ? false : eq(val.name());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "WAITING,SUCCESS,FAIL", example = "WAITING")
    public String getCode() {
        return this.name();
    }

}
