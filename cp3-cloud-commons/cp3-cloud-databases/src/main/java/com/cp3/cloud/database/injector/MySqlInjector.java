package com.cp3.cloud.database.injector;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.cp3.cloud.base.entity.SuperEntity;
import com.cp3.cloud.database.injector.method.UpdateAllById;

import java.util.List;

/**
 * 自定义sql 注入器
 *
 * @author cp3
 * @date 2020年02月19日15:39:49
 */
public class MySqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);

        //增加自定义方法
        methodList.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE));
        methodList.add(new UpdateAllById(field -> !ArrayUtil.containsAny(new String[]{
                SuperEntity.CREATE_TIME_COLUMN, SuperEntity.CREATE_USER_COLUMN
        }, field.getColumn())));
        return methodList;
    }
}
