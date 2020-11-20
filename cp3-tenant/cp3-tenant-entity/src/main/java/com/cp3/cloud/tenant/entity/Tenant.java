package com.cp3.cloud.tenant.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cp3.cloud.base.entity.Entity;
import com.cp3.cloud.tenant.enumeration.TenantConnectTypeEnum;
import com.cp3.cloud.tenant.enumeration.TenantStatusEnum;
import com.cp3.cloud.tenant.enumeration.TenantTypeEnum;
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

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;
import static com.cp3.cloud.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

/**
 * <p>
 * 实体类
 * 企业
 * </p>
 *
 * @author cp3
 * @since 2019-10-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("d_tenant")
@ApiModel(value = "Tenant", description = "企业")
public class Tenant extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 企业编码
     */
    @ApiModelProperty(value = "企业编码")
    @Length(max = 20, message = "企业编码长度不能超过20")
    @TableField(value = "code", condition = LIKE)
    @Excel(name = "企业编码", width = 20)
    private String code;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    @Length(max = 255, message = "企业名称长度不能超过255")
    @TableField(value = "name", condition = LIKE)
    @Excel(name = "企业名称", width = 20)
    private String name;

    /**
     * 类型
     * #{CREATE:创建;REGISTER:注册}
     */
    @ApiModelProperty(value = "类型")
    @TableField("type")
    @Excel(name = "类型", width = 20, replace = {"注册_REGISTER", "创建_CREATE", "_null"})
    private TenantTypeEnum type;

    @TableField("connect_type")
    @ApiModelProperty(value = "连接类型", example = "LOCAL,REMOTE")
    @Excel(name = "连接类型", width = 20, replace = {"本地_LOCAL", "远程_REMOTE", "_null"})
    private TenantConnectTypeEnum connectType;

    /**
     * 状态
     * #{NORMAL:正常;FORBIDDEN:禁用;WAITING:待审核;REFUSE:拒绝}
     */
    @ApiModelProperty(value = "状态")
    @TableField("status")
    @Excel(name = "状态", width = 20, replace = {"正常_NORMAL", "待初始化_WAIT_INIT", "禁用_FORBIDDEN", "待审核_WAITING", "拒绝_REFUSE", "DELETE_已删除", "_null"})
    private TenantStatusEnum status;

    @ApiModelProperty(value = "只读")
    @TableField("readonly")
    @Excel(name = "只读", replace = {"是_true", "否_false", "_null"})
    private Boolean readonly;

    /**
     * 责任人
     */
    @ApiModelProperty(value = "责任人")
    @Length(max = 50, message = "责任人长度不能超过50")
    @TableField(value = "duty", condition = LIKE)
    @Excel(name = "责任人")
    private String duty;

    /**
     * 有效期
     * 为空表示永久
     */
    @ApiModelProperty(value = "有效期")
    @TableField("expiration_time")
    @Excel(name = "有效期", format = DEFAULT_DATE_TIME_FORMAT, width = 20)
    private LocalDateTime expirationTime;

    /**
     * logo地址
     */
    @ApiModelProperty(value = "logo地址")
    @Length(max = 255, message = "logo地址长度不能超过255")
    @TableField(value = "logo", condition = LIKE)
    private String logo;

    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介")
    @Length(max = 255, message = "企业简介长度不能超过255")
    @TableField(value = "describe_", condition = LIKE)
    @Excel(name = "企业简介", width = 20)
    private String describe;

    @Builder
    public Tenant(Long id, LocalDateTime createTime, Long createUser, LocalDateTime updateTime, Long updateUser,
                  String code, String name, TenantTypeEnum type, TenantStatusEnum status, String duty,
                  LocalDateTime expirationTime, Boolean readonly, String logo, String describe, TenantConnectTypeEnum connectType) {
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.readonly = readonly;
        this.code = code;
        this.name = name;
        this.type = type;
        this.status = status;
        this.duty = duty;
        this.expirationTime = expirationTime;
        this.logo = logo;
        this.describe = describe;
        this.connectType = connectType;
    }

}
