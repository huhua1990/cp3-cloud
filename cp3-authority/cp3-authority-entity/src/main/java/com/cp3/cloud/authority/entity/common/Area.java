package com.cp3.cloud.authority.entity.common;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cp3.base.annotation.injection.InjectionField;
import com.cp3.base.basic.entity.TreeEntity;
import com.cp3.base.model.RemoteData;
import com.cp3.cloud.common.constant.DictionaryType;
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
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;
import static com.cp3.cloud.common.constant.InjectionFieldConstants.DICTIONARY_ITEM_CLASS;
import static com.cp3.cloud.common.constant.InjectionFieldConstants.DICTIONARY_ITEM_METHOD;

/**
 * <p>
 * 实体类
 * 地区表
 * </p>
 *
 * @author zuihou
 * @since 2020-11-20
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("c_area")
@ApiModel(value = "Area", description = "地区表")
@AllArgsConstructor
public class Area extends TreeEntity<Area, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    @NotEmpty(message = "编码不能为空")
    @Length(max = 64, message = "编码长度不能超过64")
    @TableField(value = "code", condition = LIKE)
    @Excel(name = "编码")
    private String code;

    /**
     * 全名
     */
    @ApiModelProperty(value = "全名")
    @Length(max = 255, message = "全名长度不能超过255")
    @TableField(value = "full_name", condition = LIKE)
    @Excel(name = "全名")
    private String fullName;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    @Length(max = 255, message = "经度长度不能超过255")
    @TableField(value = "longitude", condition = LIKE)
    @Excel(name = "经度")
    private String longitude;

    /**
     * 维度
     */
    @ApiModelProperty(value = "维度")
    @Length(max = 255, message = "维度长度不能超过255")
    @TableField(value = "latitude", condition = LIKE)
    @Excel(name = "维度")
    private String latitude;

    /**
     * 行政区级
     *
     * @InjectionField(api = DICTIONARY_ITEM_CLASS, method = DICTIONARY_ITEM_METHOD, dictType = DictionaryType.AREA_LEVEL) RemoteData<String, String>
     */
    @ApiModelProperty(value = "行政区级")
    @Length(max = 10, message = "行政区级长度不能超过10")
    @TableField(value = "level", condition = LIKE)
    @InjectionField(api = DICTIONARY_ITEM_CLASS, method = DICTIONARY_ITEM_METHOD, dictType = DictionaryType.AREA_LEVEL)
    @ExcelEntity(name = "")
    @Excel(name = "行政区级")
    private RemoteData<String, String> level;

    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源")
    @Length(max = 255, message = "数据来源长度不能超过255")
    @TableField(value = "source_", condition = LIKE)
    @Excel(name = "数据来源")
    private String source;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @TableField("state")
    @Excel(name = "状态", replace = {"是_true", "否_false", "_null"})
    private Boolean state;


    @Builder
    public Area(Long id, String label, Integer sortValue, Long parentId, LocalDateTime createTime, Long createdBy, LocalDateTime updateTime, Long updatedBy,
                String code, String fullName, String longitude, String latitude, RemoteData<String, String> level,
                String source, Boolean state) {
        this.id = id;
        this.label = label;
        this.sortValue = sortValue;
        this.parentId = parentId;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
        this.code = code;
        this.fullName = fullName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.level = level;
        this.source = source;
        this.state = state;
    }

}
