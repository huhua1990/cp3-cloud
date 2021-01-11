package com.cp3.cloud.activiti.dto;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.cp3.base.basic.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * 流程
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
@ApiModel(value = "FlowPageQuery", description = "流程")
public class FlowPageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程code(act_re_procdef)
     */
    @ApiModelProperty(value = "流程code(act_re_procdef)")
    private String procdefCode;
    /**
     * 流程名(act_re_procdef)
     */
    @ApiModelProperty(value = "流程名(act_re_procdef)")
    private String procdefName;
    /**
     * 流程状态
     */
    @ApiModelProperty(value = "流程状态")
    private Integer flowState;
    @ApiModelProperty(value = "")
    private Long createBy;
    @ApiModelProperty(value = "")
    private Long updateBy;

}
