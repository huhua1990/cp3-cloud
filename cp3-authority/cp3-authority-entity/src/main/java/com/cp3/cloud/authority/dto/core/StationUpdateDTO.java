package com.cp3.cloud.authority.dto.core;

import com.cp3.base.annotation.injection.InjectionField;
import com.cp3.base.basic.entity.SuperEntity;
import com.cp3.base.model.RemoteData;
import com.cp3.cloud.authority.entity.core.Org;
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
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.cp3.cloud.common.constant.InjectionFieldConstants.ORG_ID_CLASS;
import static com.cp3.cloud.common.constant.InjectionFieldConstants.ORG_ID_METHOD;

/**
 * <p>
 * 实体类
 * 岗位
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
@ApiModel(value = "StationUpdateDTO", description = "岗位")
public class StationUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @NotEmpty(message = "名称不能为空")
    @Length(max = 255, message = "名称长度不能超过255")
    private String name;
    /**
     * 组织ID
     * #c_org
     *
     * @InjectionField(api = ORG_ID_CLASS, method = ORG_ID_METHOD, beanClass = Org.class) RemoteData<Long, com.cp3.cloud.authority.entity.core.Org>
     */
    @ApiModelProperty(value = "组织ID")
    @InjectionField(api = ORG_ID_CLASS, method = ORG_ID_METHOD, beanClass = Org.class)
    private RemoteData<Long, Org> org;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Boolean state;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 255, message = "描述长度不能超过255")
    private String describe;
}
