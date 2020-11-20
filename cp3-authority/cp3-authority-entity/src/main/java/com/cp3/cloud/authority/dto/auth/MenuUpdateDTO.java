package com.cp3.cloud.authority.dto.auth;

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
 * 菜单
 * </p>
 *
 * @author cp3
 * @since 2019-11-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "MenuUpdateDTO", description = "菜单")
public class MenuUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称不能为空")
    @Length(max = 20, message = "名称长度不能超过20")
    private String label;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 200, message = "描述长度不能超过200")
    private String describe;
    /**
     * 是否公开菜单
     * 就是无需分配就可以访问的。所有人可见
     */
    @ApiModelProperty(value = "公共菜单")
    private Boolean isPublic;
    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    @Length(max = 255, message = "路径长度不能超过255")
    private String path;
    /**
     * 组件
     */
    @ApiModelProperty(value = "组件")
    @Length(max = 255, message = "组件长度不能超过255")
    private String component;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Boolean isEnable;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortValue;
    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    @Length(max = 255, message = "菜单图标长度不能超过255")
    private String icon;
    /**
     * 分组
     */
    @ApiModelProperty(value = "分组")
    @Length(max = 20, message = "分组长度不能超过20")
    private String group;
    /**
     * 父级菜单id
     */
    @ApiModelProperty(value = "父级菜单id")
    private Long parentId;

}
