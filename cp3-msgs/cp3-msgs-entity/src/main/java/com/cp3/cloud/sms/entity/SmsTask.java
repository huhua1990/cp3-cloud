package com.cp3.cloud.sms.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cp3.base.basic.entity.Entity;
import com.cp3.cloud.sms.enumeration.SourceType;
import com.cp3.cloud.sms.enumeration.TaskStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;
import static com.cp3.base.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

/**
 * <p>
 * 实体类
 * 发送任务
 * </p>
 *
 * @author zuihou
 * @since 2020-11-21
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("e_sms_task")
@ApiModel(value = "SmsTask", description = "发送任务")
@AllArgsConstructor
public class SmsTask extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     * #e_sms_template
     */
    @ApiModelProperty(value = "模板ID")
    @NotNull(message = "模板ID不能为空")
    @TableField("template_id")
    @Excel(name = "模板ID")
    private Long templateId;

    /**
     * 执行状态
     * (手机号具体发送状态看sms_send_status表)
     * #TaskStatus{WAITING:等待执行;SUCCESS:执行成功;FAIL:执行失败}
     */
    @ApiModelProperty(value = "执行状态")
    @TableField("status")
    @Excel(name = "执行状态", replace = {"等待执行_WAITING", "执行成功_SUCCESS", "执行失败_FAIL", "_null"})
    private TaskStatus status;

    /**
     * 来源类型
     * #SourceType{APP:应用;SERVICE:服务}
     */
    @ApiModelProperty(value = "来源类型")
    @TableField("source_type")
    @Excel(name = "来源类型", replace = {"应用_APP", "服务_SERVICE", "_null"})
    private SourceType sourceType;

    /**
     * 接收者手机号
     * 群发用英文逗号分割.
     * 支持2种 格式:1: 手机号,手机号  格式2: 姓名<手机号>,姓名<手机号>
     */
    @ApiModelProperty(value = "接收者手机号")
    @Length(max = 65535, message = "接收者手机号长度不能超过65535")
    @TableField("receiver")
    @Excel(name = "接收者手机号")
    private String receiver;

    /**
     * 主题
     */
    @ApiModelProperty(value = "主题")
    @Length(max = 255, message = "主题长度不能超过255")
    @TableField(value = "topic", condition = LIKE)
    @Excel(name = "主题")
    private String topic;

    /**
     * 参数
     * 需要封装为{‘key’:’value’, ...}格式且key必须有序
     */
    @ApiModelProperty(value = "参数")
    @Length(max = 500, message = "参数长度不能超过500")
    @TableField(value = "template_params", condition = LIKE)
    @Excel(name = "参数")
    private String templateParams;

    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("send_time")
    @Excel(name = "发送时间", format = DEFAULT_DATE_TIME_FORMAT, width = 20)
    private LocalDateTime sendTime;

    /**
     * 发送内容
     * 需要封装正确格式化: 您好，张三，您有一个新的快递。
     */
    @ApiModelProperty(value = "发送内容")
    @Length(max = 500, message = "发送内容长度不能超过500")
    @TableField(value = "content", condition = LIKE)
    @Excel(name = "发送内容")
    private String content;

    /**
     * 是否草稿
     */
    @ApiModelProperty(value = "是否草稿")
    @TableField("draft")
    @Excel(name = "是否草稿", replace = {"是_true", "否_false", "_null"})
    private Boolean draft;


    @Builder
    public SmsTask(Long id, Long createdBy, LocalDateTime createTime, Long updatedBy, LocalDateTime updateTime,
                   Long templateId, TaskStatus status, SourceType sourceType, String receiver, String topic,
                   String templateParams, LocalDateTime sendTime, String content, Boolean draft) {
        this.id = id;
        this.createdBy = createdBy;
        this.createTime = createTime;
        this.updatedBy = updatedBy;
        this.updateTime = updateTime;
        this.templateId = templateId;
        this.status = status;
        this.sourceType = sourceType;
        this.receiver = receiver;
        this.topic = topic;
        this.templateParams = templateParams;
        this.sendTime = sendTime;
        this.content = content;
        this.draft = draft;
    }

}
