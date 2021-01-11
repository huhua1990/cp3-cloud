package com.cp3.cloud.activiti.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.cp3.base.basic.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.cp3.cloud.common.constant.DictionaryType;
import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 流程规则
 * </p>
 *
 * @author cp3
 * @since 2021-01-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "FlowRuleSaveDTO", description = "流程规则")
public class FlowRuleSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程id
     */
    @ApiModelProperty(value = "流程id")
    private Long flowId;
    /**
     * 系统类型
     */
    @ApiModelProperty(value = "系统类型")
    @Length(max = 32, message = "系统类型长度不能超过32")
    private String systemCode;
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    @Length(max = 32, message = "业务类型长度不能超过32")
    private String busiType;
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    @Length(max = 255, message = "规则名称长度不能超过255")
    private String ruleName;
    /**
     * 规则描述
     */
    @ApiModelProperty(value = "规则描述")
    @Length(max = 255, message = "规则描述长度不能超过255")
    private String ruleDesc;
    @ApiModelProperty(value = "")
    private Long createBy;
    @ApiModelProperty(value = "")
    private Long updateBy;

}
