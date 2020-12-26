package com.cp3.cloud.sms.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cp3.base.basic.entity.Entity;
import com.cp3.cloud.sms.enumeration.ProviderType;
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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * <p>
 * 实体类
 * 短信模板
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
@TableName("e_sms_template")
@ApiModel(value = "SmsTemplate", description = "短信模板")
@AllArgsConstructor
public class SmsTemplate extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商类型
     * #ProviderType{ALI:OK,阿里云短信;TENCENT:0,腾讯云短信;BAIDU:1000,百度云短信}
     */
    @ApiModelProperty(value = "供应商类型")
    @NotNull(message = "供应商类型不能为空")
    @TableField("provider_type")
    @Excel(name = "供应商类型", replace = {"OK_ALI", "0_TENCENT", "1000_BAIDU", "_null"})
    private ProviderType providerType;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    @NotEmpty(message = "应用ID不能为空")
    @Length(max = 255, message = "应用ID长度不能超过255")
    @TableField(value = "app_id", condition = LIKE)
    @Excel(name = "应用ID")
    private String appId;

    /**
     * 应用密码
     */
    @ApiModelProperty(value = "应用密码")
    @NotEmpty(message = "应用密码不能为空")
    @Length(max = 255, message = "应用密码长度不能超过255")
    @TableField(value = "app_secret", condition = LIKE)
    @Excel(name = "应用密码")
    private String appSecret;

    /**
     * SMS服务域名
     * 百度、其他厂商会用
     */
    @ApiModelProperty(value = "SMS服务域名")
    @Length(max = 255, message = "SMS服务域名长度不能超过255")
    @TableField(value = "url", condition = LIKE)
    @Excel(name = "SMS服务域名")
    private String url;

    /**
     * 模板编码
     * 用于api发送
     */
    @ApiModelProperty(value = "模板编码")
    @NotEmpty(message = "模板编码不能为空")
    @Length(max = 20, message = "模板编码长度不能超过20")
    @TableField(value = "custom_code", condition = LIKE)
    @Excel(name = "模板编码")
    private String customCode;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    @Length(max = 255, message = "模板名称长度不能超过255")
    @TableField(value = "name", condition = LIKE)
    @Excel(name = "模板名称")
    private String name;

    /**
     * 模板内容
     */
    @ApiModelProperty(value = "模板内容")
    @NotEmpty(message = "模板内容不能为空")
    @Length(max = 255, message = "模板内容长度不能超过255")
    @TableField(value = "content", condition = LIKE)
    @Excel(name = "模板内容")
    private String content;

    /**
     * 模板参数
     */
    @ApiModelProperty(value = "模板参数")
    @NotEmpty(message = "模板参数不能为空")
    @Length(max = 255, message = "模板参数长度不能超过255")
    @TableField(value = "template_params", condition = LIKE)
    @Excel(name = "模板参数")
    private String templateParams;

    /**
     * 模板CODE
     */
    @ApiModelProperty(value = "模板CODE")
    @NotEmpty(message = "模板CODE不能为空")
    @Length(max = 50, message = "模板CODE长度不能超过50")
    @TableField(value = "template_code", condition = LIKE)
    @Excel(name = "模板CODE")
    private String templateCode;

    /**
     * 签名
     */
    @ApiModelProperty(value = "签名")
    @Length(max = 100, message = "签名长度不能超过100")
    @TableField(value = "sign_name", condition = LIKE)
    @Excel(name = "签名")
    private String signName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 255, message = "备注长度不能超过255")
    @TableField(value = "template_describe", condition = LIKE)
    @Excel(name = "备注")
    private String templateDescribe;


    @Builder
    public SmsTemplate(Long id, Long createdBy, LocalDateTime createTime, Long updatedBy, LocalDateTime updateTime,
                       ProviderType providerType, String appId, String appSecret, String url, String customCode,
                       String name, String content, String templateParams, String templateCode, String signName, String templateDescribe) {
        this.id = id;
        this.createdBy = createdBy;
        this.createTime = createTime;
        this.updatedBy = updatedBy;
        this.updateTime = updateTime;
        this.providerType = providerType;
        this.appId = appId;
        this.appSecret = appSecret;
        this.url = url;
        this.customCode = customCode;
        this.name = name;
        this.content = content;
        this.templateParams = templateParams;
        this.templateCode = templateCode;
        this.signName = signName;
        this.templateDescribe = templateDescribe;
    }

}
