package com.cp3.cloud.authority.dto.auth;

import com.cp3.cloud.authority.enumeration.auth.ApplicationAppTypeEnum;
import com.cp3.cloud.base.entity.SuperEntity;
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
 * 应用
 * </p>
 *
 * @author cp3
 * @since 2020-04-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "ApplicationUpdateDTO", description = "应用")
public class ApplicationUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 官网
     */
    @ApiModelProperty(value = "官网")
    @Length(max = 100, message = "官网长度不能超过100")
    private String website;
    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    @NotEmpty(message = "应用名称不能为空")
    @Length(max = 255, message = "应用名称长度不能超过255")
    private String name;
    /**
     * 应用图标
     */
    @ApiModelProperty(value = "应用图标")
    @Length(max = 255, message = "应用图标长度不能超过255")
    private String icon;
    /**
     * 类型
     * #{SERVER:服务应用;APP:手机应用;PC:PC网页应用;WAP:手机网页应用}
     *
     */
    @ApiModelProperty(value = "类型")
    private ApplicationAppTypeEnum appType;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注长度不能超过200")
    private String describe;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Boolean status;
}
