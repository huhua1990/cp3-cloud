package com.cp3.cloud.database.mybatis.conditions;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.cp3.cloud.database.mybatis.conditions.query.LbqWrapper;
import com.cp3.cloud.database.mybatis.conditions.query.QueryWrap;
import com.cp3.cloud.database.mybatis.conditions.update.LbuWrapper;
import com.cp3.cloud.model.RemoteData;
import com.cp3.cloud.utils.DateUtils;
import com.cp3.cloud.utils.StrHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static com.cp3.cloud.utils.StrPool.PERCENT;
import static com.cp3.cloud.utils.StrPool.UNDERSCORE;

/**
 * Wrappers 工具类， 该方法的主要目的是为了 缩短代码长度
 *
 * @author cp3
 * @date 2019/06/14
 */
public class Wraps {

    private Wraps() {
        // ignore
    }

    /**
     * 获取 QueryWrap&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return QueryWrapper&lt;T&gt;
     */
    public static <T> QueryWrap<T> q() {
        return new QueryWrap<>();
    }

    /**
     * 获取 QueryWrap&lt;T&gt;
     *
     * @param entity 实体类
     * @param <T>    实体类泛型
     * @return QueryWrapper&lt;T&gt;
     */
    public static <T> QueryWrap<T> q(T entity) {
        return new QueryWrap<>(entity);
    }

    /**
     * 获取 HyLambdaQueryWrapper&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return LambdaQueryWrapper&lt;T&gt;
     */
    public static <T> LbqWrapper<T> lbQ() {
        return new LbqWrapper<>();
    }

    /**
     * 获取 HyLambdaQueryWrapper&lt;T&gt;
     *
     * @param entity 实体类
     * @param <T>    实体类泛型
     * @return LambdaQueryWrapper&lt;T&gt;
     */
    public static <T> LbqWrapper<T> lbQ(T entity) {
        return new LbqWrapper<>(entity);
    }

    /**
     * 获取 HyLambdaQueryWrapper&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return LambdaUpdateWrapper&lt;T&gt;
     */
    public static <T> LbuWrapper<T> lbU() {
        return new LbuWrapper<>();
    }

    /**
     * 获取 HyLambdaQueryWrapper&lt;T&gt;
     *
     * @param entity 实体类
     * @param <T>    实体类泛型
     * @return LambdaUpdateWrapper&lt;T&gt;
     */
    public static <T> LbuWrapper<T> lbU(T entity) {
        return new LbuWrapper<>(entity);
    }


    public static <Entity> LbqWrapper<Entity> lbq(Entity model, Map<String, String> map, Class<Entity> modelClazz) {
        return q(model, map, modelClazz).lambda();
    }

    public static <Entity> QueryWrap<Entity> q(Entity model, Map<String, String> map, Class<Entity> modelClazz) {
        QueryWrap<Entity> wrapper = model != null ? Wraps.q(model) : Wraps.q();

        if (CollUtil.isNotEmpty(map)) {
            //拼装区间
            for (Map.Entry<String, String> field : map.entrySet()) {
                String key = field.getKey();
                String value = field.getValue();
                if (StrUtil.isEmpty(value)) {
                    continue;
                }
                if (key.endsWith("_st")) {
                    String beanField = StrUtil.subBefore(key, "_st", true);
                    wrapper.ge(getDbField(beanField, modelClazz), DateUtils.getStartTime(value));
                }
                if (key.endsWith("_ed")) {
                    String beanField = StrUtil.subBefore(key, "_ed", true);
                    wrapper.le(getDbField(beanField, modelClazz), DateUtils.getEndTime(value));
                }
            }
        }
        return wrapper;
    }


    /**
     * 根据 bean字段 反射出 数据库字段
     *
     * @param beanField
     * @param clazz
     * @return
     */
    public static String getDbField(String beanField, Class<?> clazz) {
        Field field = ReflectUtil.getField(clazz, beanField);
        if (field == null) {
            return StrUtil.EMPTY;
        }
        TableField tf = field.getAnnotation(TableField.class);
        if (tf != null && StrUtil.isNotEmpty(tf.value())) {
            String str = tf.value();
            return str;
        }
        return StrUtil.EMPTY;
    }


    /**
     * 替换 实体对象中类型为String 类型的参数，并将% 和 _ 符号转义
     *
     * @param source 源对象
     * @return 最新源对象
     * @see
     */
    public static <T> T replace(Object source) {
        if (source == null) {
            return null;
        }
        Object target = source;

        Class<?> srcClass = source.getClass();
        Field[] fields = ReflectUtil.getFields(srcClass);
        for (Field field : fields) {
            Object classValue = ReflectUtil.getFieldValue(source, field);
            if (classValue == null) {
                continue;
            }
            //final 和 static 字段跳过
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (classValue instanceof RemoteData) {
                RemoteData rd = (RemoteData) classValue;
                Object key = rd.getKey();
                if (ObjectUtil.isEmpty(key)) {
                    ReflectUtil.setFieldValue(target, field, null);
                    continue;
                }
                if (!(key instanceof String)) {
                    continue;
                }
                String strKey = (String) key;
                if (strKey.contains(PERCENT) || strKey.contains(UNDERSCORE)) {
                    String tarValue = StrHelper.keywordConvert(strKey);
                    rd.setKey(tarValue);
                    ReflectUtil.setFieldValue(target, field, rd);
                }
                continue;
            }

            if (!(classValue instanceof String)) {
                continue;
            }
            String srcValue = (String) classValue;
            if (srcValue.contains(PERCENT) || srcValue.contains(UNDERSCORE)) {
                String tarValue = StrHelper.keywordConvert(srcValue);
                ReflectUtil.setFieldValue(target, field, tarValue);
            }
        }
        return (T) target;
    }


}
