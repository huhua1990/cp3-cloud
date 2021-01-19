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
 * 流程
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
@TableName("b_flow")
@ApiModel(value = "Flow", description = "流程")
@AllArgsConstructor
public class Flow extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 流程code(act_re_procdef)
     */
    @ApiModelProperty(value = "流程code(act_re_procdef)")
    @Length(max = 255, message = "流程code(act_re_procdef)长度不能超过255")
    @TableField(value = "procdef_code", condition = LIKE)
    @Excel(name = "流程code(act_re_procdef)")
    private String procdefCode;

    /**
     * 流程名(act_re_procdef)
     */
    @ApiModelProperty(value = "流程名(act_re_procdef)")
    @Length(max = 255, message = "流程名(act_re_procdef)长度不能超过255")
    @TableField(value = "procdef_name", condition = LIKE)
    @Excel(name = "流程名(act_re_procdef)")
    private String procdefName;

    /**
     * 流程状态
     */
    @ApiModelProperty(value = "流程状态")
    @TableField("flow_state")
    @Excel(name = "流程状态")
    private Integer flowState;

    @Builder
    public Flow(Long id, LocalDateTime createTime, LocalDateTime updateTime,
                    String procdefCode, String procdefName, Integer flowState, Long createdBy, Long updatedBy) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.procdefCode = procdefCode;
        this.procdefName = procdefName;
        this.flowState = flowState;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

}
