package com.cp3.cloud.authority.dto.auth;

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
import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 菜单
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
@ApiModel(value = "MenuSaveDTO", description = "菜单")
public class MenuSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 200, message = "描述长度不能超过200")
    private String describe;
    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称不能为空")
    @Length(max = 255, message = "名称长度不能超过255")
    protected String label;
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
    @ApiModelProperty(value = "父ID")
    protected Long parentId;
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
    @ApiModelProperty(value = "排序号")
    protected Integer sortValue;
    /**
     * 通用菜单
     * True表示无需分配所有人就可以访问的
     */
    @ApiModelProperty(value = "通用菜单")
    private Boolean isGeneral;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Boolean state;
    /**
     * 内置
     */
    @ApiModelProperty(value = "内置")
    private Boolean readonly;
}
