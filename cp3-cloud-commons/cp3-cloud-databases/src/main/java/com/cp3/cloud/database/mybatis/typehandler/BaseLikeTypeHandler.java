package com.cp3.cloud.database.mybatis.typehandler;

import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.cp3.cloud.utils.StrHelper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 仅仅用于like查询
 *
 * @author cp3
 */
public class BaseLikeTypeHandler extends BaseTypeHandler<CharSequence> {

    private final SqlLike likeType;

    public BaseLikeTypeHandler(SqlLike likeType) {
        this.likeType = likeType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CharSequence parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setString(i, null);
        } else {
            ps.setString(i, like(parameter.toString()));
        }
    }

    private String like(String parameter) {
        return StrHelper.like(parameter, likeType);
    }


    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }

}
