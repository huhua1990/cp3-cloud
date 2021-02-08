package com.cp3.cloud.activiti.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.cp3.base.basic.entity.Entity;
import java.time.LocalDateTime;
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
 * 假期申请
 * </p>
 *
 * @author cp3
 * @since 2021-01-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "LeaveSaveDTO", description = "假期申请")
public class LeaveSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    /**
     * 请假类型
     */
    @ApiModelProperty(value = "请假类型")
    private Integer leaveType;
    /**
     * 请假说明
     */
    @ApiModelProperty(value = "请假说明")
    @Length(max = 255, message = "请假说明长度不能超过255")
    private String context;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer state;

}
