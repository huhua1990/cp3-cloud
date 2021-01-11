package com.cp3.cloud.activiti.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.cp3.cloud.common.constant.DictionaryType;
import static com.cp3.base.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_flow_rule")
@ApiModel(value = "FlowRule", description = "流程规则")
@AllArgsConstructor
public class FlowRule extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 流程id
     */
    @ApiModelProperty(value = "流程id")
    @TableField("flow_id")
    @Excel(name = "流程id")
    private Long flowId;

    /**
     * 系统类型
     */
    @ApiModelProperty(value = "系统类型")
    @Length(max = 32, message = "系统类型长度不能超过32")
    @TableField(value = "system_code", condition = LIKE)
    @Excel(name = "系统类型")
    private String systemCode;

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    @Length(max = 32, message = "业务类型长度不能超过32")
    @TableField(value = "busi_type", condition = LIKE)
    @Excel(name = "业务类型")
    private String busiType;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    @Length(max = 255, message = "规则名称长度不能超过255")
    @TableField(value = "rule_name", condition = LIKE)
    @Excel(name = "规则名称")
    private String ruleName;

    /**
     * 规则描述
     */
    @ApiModelProperty(value = "规则描述")
    @Length(max = 255, message = "规则描述长度不能超过255")
    @TableField(value = "rule_desc", condition = LIKE)
    @Excel(name = "规则描述")
    private String ruleDesc;

    @ApiModelProperty(value = "")
    @TableField("create_by")
    @Excel(name = "")
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField("update_by")
    @Excel(name = "")
    private Long updateBy;


    @Builder
    public FlowRule(Long id, LocalDateTime createTime, LocalDateTime updateTime, 
                    Long flowId, String systemCode, String busiType, String ruleName, String ruleDesc, 
                    Long createBy, Long updateBy) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.flowId = flowId;
        this.systemCode = systemCode;
        this.busiType = busiType;
        this.ruleName = ruleName;
        this.ruleDesc = ruleDesc;
        this.createBy = createBy;
        this.updateBy = updateBy;
    }

}
