package com.cp3.cloud.injection.mybatis.typehandler;

import com.cp3.cloud.model.RemoteData;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 远程数据 类型转换器
 * <p>
 * 借鉴Hibernate的外键字段的思想，自定义一个类型处理器，
 * 将数据库中 bigint、int、varchar类型的字段映射成JavaBean中 RemoteData 类型的字段
 *
 * @author cp3
 * @date 2020年01月18日17:20:34
 */
public class RemoteDataTypeHandler extends BaseTypeHandler<RemoteData> {
    /**
     * insert 、update 时执行该方法
     *
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, RemoteData parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter != null && parameter.getKey() != null) {
            if (parameter.getKey() instanceof String) {
                ps.setString(i, (String) parameter.getKey());
            } else if (parameter.getKey() instanceof Long) {
                ps.setLong(i, (Long) parameter.getKey());
            } else if (parameter.getKey() instanceof Integer) {
                ps.setInt(i, (Integer) parameter.getKey());
            } else {
                ps.setObject(i, parameter.getKey());
            }
        } else {
            ps.setNull(i, Types.BIGINT);
        }
    }

    @Override
    public RemoteData getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object key = rs.getObject(columnName);
        return build(key);
    }

    @Override
    public RemoteData getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object key = rs.getObject(columnIndex);
        return build(key);
    }

    @Override
    public RemoteData getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object key = cs.getObject(columnIndex);
        return build(key);
    }

    private RemoteData build(Object key) {
        if (key == null) {
            return new RemoteData();
        }
        return new RemoteData(key);
    }

}
