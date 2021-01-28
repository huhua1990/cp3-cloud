package com.cp3.cloud.flowable.entity;

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
 * @since 2021-01-25
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("f_flow")
@ApiModel(value = "Flow", description = "流程")
@AllArgsConstructor
public class Flow extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 流程模板id(act_re_model)
     */
    @ApiModelProperty(value = "流程模板id(act_re_model)")
    @Length(max = 32, message = "流程模板id(act_re_model)长度不能超过32")
    @TableField(value = "model_id", condition = LIKE)
    @Excel(name = "流程模板id(act_re_model)")
    private String modelId;

    /**
     * 流程部署id(act_re_procdef)
     */
    @ApiModelProperty(value = "流程部署id(act_re_procdef)")
    @Length(max = 32, message = "流程部署id(act_re_procdef)长度不能超过32")
    @TableField(value = "procdef_id", condition = LIKE)
    @Excel(name = "流程部署id(act_re_procdef)")
    private String procdefId;


    @Builder
    public Flow(Long id, Long createdBy, LocalDateTime createTime, Long updatedBy, LocalDateTime updateTime,
                    String modelId, String procdefId) {
        this.id = id;
        this.createdBy = createdBy;
        this.createTime = createTime;
        this.updatedBy = updatedBy;
        this.updateTime = updateTime;
        this.modelId = modelId;
        this.procdefId = procdefId;
    }

}
