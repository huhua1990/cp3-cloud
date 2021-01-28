package com.cp3.cloud.flowable.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
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
 * 假期申请
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
@TableName("f_leave")
@ApiModel(value = "Leave", description = "假期申请")
@AllArgsConstructor
public class Leave extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @TableField("start_time")
    @Excel(name = "开始时间", format = DEFAULT_DATE_TIME_FORMAT, width = 20)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    @Excel(name = "结束时间", format = DEFAULT_DATE_TIME_FORMAT, width = 20)
    private LocalDateTime endTime;

    /**
     * 请假类型
     */
    @ApiModelProperty(value = "请假类型")
    @TableField("leave_type")
    @Excel(name = "请假类型")
    private Integer leaveType;

    /**
     * 请假说明
     */
    @ApiModelProperty(value = "请假说明")
    @Length(max = 255, message = "请假说明长度不能超过255")
    @TableField(value = "context", condition = LIKE)
    @Excel(name = "请假说明")
    private String context;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @TableField("state")
    @Excel(name = "状态")
    private Integer state;


    @Builder
    public Leave(Long id, LocalDateTime createTime, Long createdBy, LocalDateTime updateTime, Long updatedBy,
                    Long userId, LocalDateTime startTime, LocalDateTime endTime, Integer leaveType, String context, Integer state) {
        this.id = id;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.leaveType = leaveType;
        this.context = context;
        this.state = state;
    }

}
