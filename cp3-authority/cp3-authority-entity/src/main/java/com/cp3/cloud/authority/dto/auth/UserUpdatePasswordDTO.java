package com.cp3.cloud.authority.dto.auth;

import com.cp3.base.basic.entity.SuperEntity;
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
import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 用户
 * </p>
 *
 * @author zuihou
 * @since 2019-11-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "UserUpdatePasswordDTO", description = "用户")
public class UserUpdatePasswordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 密码
     */
    @ApiModelProperty(value = "旧密码")
    @Length(max = 64, message = "旧密码长度不能超过64")
    private String oldPassword;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    @Length(max = 64, message = "密码长度不能超过64")
    private String password;

    /**
     * 密码
     */
    @ApiModelProperty(value = "确认密码")
    @NotEmpty(message = "确认密码不能为空")
    @Length(max = 64, message = "确认密码长度不能超过64")
    private String confirmPassword;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码，cp3-ui页面使用")
    private String tenantCode;
}
