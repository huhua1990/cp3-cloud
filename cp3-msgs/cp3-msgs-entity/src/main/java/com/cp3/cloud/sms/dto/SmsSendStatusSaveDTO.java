package com.cp3.cloud.sms.dto;

import com.cp3.cloud.sms.enumeration.SendStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 短信发送状态
 * </p>
 *
 * @author cp3
 * @since 2019-08-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "SmsSendStatusSaveDTO", description = "短信发送状态")
public class SmsSendStatusSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     * #sms_task
     */
    @ApiModelProperty(value = "任务ID")
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    /**
     * 发送状态
     * #SendStatus{WAITING:等待发送;SUCCESS:发送成功;FAIL:发送失败}
     */
    @ApiModelProperty(value = "发送状态")
    @NotNull(message = "发送状态不能为空")
    private SendStatus sendStatus;
    /**
     * 接收者手机号
     * 单个手机号
     */
    @ApiModelProperty(value = "接收者手机号")
    @NotEmpty(message = "接收者手机号不能为空")
    @Length(max = 20, message = "接收者手机号长度不能超过20")
    private String receiver;
    /**
     * 发送回执ID
     * 阿里：发送回执ID,可根据该ID查询具体的发送状态  腾讯：sid 标识本次发送id，标识一次短信下发记录  百度：requestId 短信发送请求唯一流水ID
     */
    @ApiModelProperty(value = "发送回执ID")
    @Length(max = 255, message = "发送回执ID长度不能超过255")
    private String bizId;
    /**
     * 发送返回
     * 阿里：RequestId 请求ID  腾讯：ext：用户的session内容，腾讯server回包中会原样返回   百度：无
     */
    @ApiModelProperty(value = "发送返回")
    @Length(max = 255, message = "发送返回长度不能超过255")
    private String ext;
    /**
     * 状态码
     * 阿里：返回OK代表请求成功,其他错误码详见错误码列表  腾讯：0表示成功(计费依据)，非0表示失败  百度：1000 表示成功
     */
    @ApiModelProperty(value = "状态码")
    @Length(max = 255, message = "状态码长度不能超过255")
    private String code;
    /**
     * 状态码的描述
     */
    @ApiModelProperty(value = "状态码的描述")
    @Length(max = 500, message = "状态码的描述长度不能超过500")
    private String message;
    /**
     * 短信计费的条数
     * 腾讯专用
     */
    @ApiModelProperty(value = "短信计费的条数")
    private Integer fee;
    /**
     * 创建时年月
     * 格式：yyyy-MM 用于统计
     */
    @ApiModelProperty(value = "创建时年月")
    @Length(max = 7, message = "创建时年月长度不能超过7")
    private String createMonth;
    /**
     * 创建时年周
     * 创建时处于当年的第几周 yyyy-ww 用于统计
     */
    @ApiModelProperty(value = "创建时年周")
    @Length(max = 10, message = "创建时年周长度不能超过10")
    private String createWeek;
    /**
     * 创建时年月日
     * 格式： yyyy-MM-dd 用于统计
     */
    @ApiModelProperty(value = "创建时年月日")
    @Length(max = 10, message = "创建时年月日长度不能超过10")
    private String createDate;

}
