package com.cp3.base.database.datasource;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cp3.base.basic.entity.Entity;
import com.cp3.base.basic.entity.SuperEntity;
import com.cp3.base.context.ContextUtil;
import com.cp3.base.utils.SpringUtils;
import com.cp3.base.utils.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * MyBatis Plus 元数据处理类
 * 用于自动 注入 id, createTime, updateTime, createdBy, updatedBy 等字段
 * <p>
 * 判断逻辑：
 * 1. insert 方法，自动填充 id, createTime, updateTime, createdBy, updatedBy 字段，字段为空则自动生成，不为空则使用传递进来的
 * 2. update 方法，自动填充 id, updateTime, updatedBy 字段，字段为空则自动生成，不为空则使用传递进来的
 * <p>
 * 注入值：
 * id：  IdUtil.getSnowflake(workerId, dataCenterId);
 * createTime：LocalDateTime.now()
 * updateTime：LocalDateTime.now()
 * createdBy：BaseContextHandler.getUserId()
 * updatedBy：BaseContextHandler.getUserId()
 *
 * @author zuihou
 * @date 2019/04/29
 */
@Slf4j
public class LampMetaObjectHandler implements MetaObjectHandler {

    private UidGenerator uidGenerator;

    public LampMetaObjectHandler() {
        super();
    }

    /**
     * 注意：不支持 复合主键 自动注入！！
     * <p>
     * 所有的继承了Entity、SuperEntity的实体，在insert时，
     * id： id为空时， 通过IdGenerate生成唯一ID， 不为空则使用传递进来的id
     * createdBy, updatedBy: 自动赋予 当前线程上的登录人id
     * createTime, updateTime: 自动赋予 服务器的当前时间
     * <p>
     * 未继承任何父类的实体，且主键标注了 @TableId(value = "xxx", type = IdType.INPUT) 自动注入 主键
     * 主键的字段名称任意
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        fillCreated(metaObject);
        fillUpdated(metaObject);
        fillId(metaObject);
    }

    private void fillId(MetaObject metaObject) {
        if (uidGenerator == null) {
            // 这里使用SpringUtils的方式"延迟"获取对象，防止启动时，报循环注入的错
            uidGenerator = SpringUtils.getBean(UidGenerator.class);
        }
        Long id = uidGenerator.getUid();

        //1. 继承了SuperEntity 若 ID 中有值，就不设置
        if (metaObject.getOriginalObject() instanceof SuperEntity) {
            Object oldId = ((SuperEntity) metaObject.getOriginalObject()).getId();
            if (oldId != null) {
                return;
            }
            Object idVal = StrPool.STRING_TYPE_NAME.equals(metaObject.getGetterType(SuperEntity.FIELD_ID).getName()) ? String.valueOf(id) : id;
            this.setFieldValByName(SuperEntity.FIELD_ID, idVal, metaObject);
            return;
        }

        // 2. 没有继承SuperEntity， 但主键的字段名为：  id
        if (metaObject.hasGetter(SuperEntity.FIELD_ID)) {
            Object oldId = metaObject.getValue(SuperEntity.FIELD_ID);
            if (oldId != null) {
                return;
            }

            Object idVal = StrPool.STRING_TYPE_NAME.equals(metaObject.getGetterType(SuperEntity.FIELD_ID).getName()) ? String.valueOf(id) : id;
            this.setFieldValByName(SuperEntity.FIELD_ID, idVal, metaObject);
            return;
        }

        // 3. 实体没有继承 Entity 和 SuperEntity，且 主键名为其他字段
        TableInfo tableInfo = metaObject.hasGetter(Constants.MP_OPTLOCK_ET_ORIGINAL) ?
                TableInfoHelper.getTableInfo(metaObject.getValue(Constants.MP_OPTLOCK_ET_ORIGINAL).getClass())
                : TableInfoHelper.getTableInfo(metaObject.getOriginalObject().getClass());
        if (tableInfo == null) {
            return;
        }
        // 主键类型
        Class<?> keyType = tableInfo.getKeyType();
        if (keyType == null) {
            return;
        }
        // id 字段名
        String keyProperty = tableInfo.getKeyProperty();
        Object oldId = metaObject.getValue(keyProperty);
        if (oldId != null) {
            return;
        }

        // 反射得到 主键的值
        Field idField = ReflectUtil.getField(metaObject.getOriginalObject().getClass(), keyProperty);
        Object fieldValue = ReflectUtil.getFieldValue(metaObject.getOriginalObject(), idField);
        // 判断ID 是否有值，有值就不
        if (ObjectUtil.isNotEmpty(fieldValue)) {
            return;
        }
        Object idVal = keyType.getName().equalsIgnoreCase(StrPool.STRING_TYPE_NAME) ? String.valueOf(id) : id;
        this.setFieldValByName(keyProperty, idVal, metaObject);
    }

    private void fillCreated(MetaObject metaObject) {
        // 设置创建时间和创建人
        if (metaObject.getOriginalObject() instanceof SuperEntity) {
            created(metaObject);
            return;
        }

        if (metaObject.hasGetter(Entity.CREATED_BY)) {
            Object oldVal = metaObject.getValue(Entity.CREATED_BY);
            if (oldVal == null) {
                this.setFieldValByName(Entity.CREATED_BY, ContextUtil.getUserId(), metaObject);
            }
        }
        if (metaObject.hasGetter(Entity.CREATE_TIME)) {
            Object oldVal = metaObject.getValue(Entity.CREATE_TIME);
            if (oldVal == null) {
                this.setFieldValByName(Entity.CREATE_TIME, LocalDateTime.now(), metaObject);
            }
        }

    }

    private void created(MetaObject metaObject) {
        SuperEntity entity = (SuperEntity) metaObject.getOriginalObject();
        if (entity.getCreateTime() == null) {
            this.setFieldValByName(Entity.CREATE_TIME, LocalDateTime.now(), metaObject);
        }
        if (entity.getCreatedBy() == null || entity.getCreatedBy().equals(0)) {
            Object userIdVal = StrPool.STRING_TYPE_NAME.equals(metaObject.getGetterType(SuperEntity.CREATED_BY).getName()) ? String.valueOf(ContextUtil.getUserId()) : ContextUtil.getUserId();
            this.setFieldValByName(Entity.CREATED_BY, userIdVal, metaObject);
        }
    }


    private void fillUpdated(MetaObject metaObject) {
        // 修改人 修改时间
        if (metaObject.getOriginalObject() instanceof Entity) {
            update(metaObject);
            return;
        }

        if (metaObject.hasGetter(Entity.UPDATED_BY)) {
            Object oldVal = metaObject.getValue(Entity.UPDATED_BY);
            if (oldVal == null) {
                this.setFieldValByName(Entity.UPDATED_BY, ContextUtil.getUserId(), metaObject);
            }
        }
        if (metaObject.hasGetter(Entity.UPDATE_TIME)) {
            Object oldVal = metaObject.getValue(Entity.UPDATE_TIME);
            if (oldVal == null) {
                this.setFieldValByName(Entity.UPDATE_TIME, LocalDateTime.now(), metaObject);
            }
        }
    }

    private void update(MetaObject metaObject) {
        Entity entity = (Entity) metaObject.getOriginalObject();
        if (entity.getUpdatedBy() == null || entity.getUpdatedBy().equals(0)) {
            Object userIdVal = StrPool.STRING_TYPE_NAME.equals(metaObject.getGetterType(Entity.UPDATED_BY).getName()) ? String.valueOf(ContextUtil.getUserId()) : ContextUtil.getUserId();
            this.setFieldValByName(Entity.UPDATED_BY, userIdVal, metaObject);
        }
        if (entity.getUpdateTime() == null) {
            this.setFieldValByName(Entity.UPDATE_TIME, LocalDateTime.now(), metaObject);
        }
    }

    /**
     * 所有的继承了Entity、SuperEntity的实体，在update时，
     * updatedBy: 自动赋予 当前线程上的登录人id
     * updateTime: 自动赋予 服务器的当前时间
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        fillUpdated(metaObject);
    }
}
