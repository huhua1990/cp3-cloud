package com.cp3.cloud.authority.dto.auth;

import com.cp3.base.database.mybatis.auth.DataScopeType;
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
import java.util.List;

/**
 * <p>
 * 实体类
 * 角色
 * </p>
 *
 * @author zuihou
 * @since 2020-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "RoleSaveDTO", description = "角色")
public class RoleSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称不能为空")
    @Length(max = 30, message = "名称长度不能超过30")
    private String name;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    @Length(max = 20, message = "编码长度不能超过20")
    private String code;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 100, message = "描述长度不能超过100")
    private String describe;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Boolean state;
    /**
     * 内置角色
     */
    @ApiModelProperty(value = "内置角色")
    private Boolean readonly;
    /**
     * 数据权限
     * #DataScopeType{ALL:1,全部;THIS_LEVEL:2,本级;THIS_LEVEL_CHILDREN:3,本级以及子级;CUSTOMIZE:4,自定义;SELF:5,个人;}
     */
    @ApiModelProperty(value = "数据权限")
    @NotNull(message = "数据权限不能为空")
    private DataScopeType dsType;
    /**
     * 关联的组织id
     */
    @ApiModelProperty(value = "关联的组织id")
    private List<Long> orgList;
}
