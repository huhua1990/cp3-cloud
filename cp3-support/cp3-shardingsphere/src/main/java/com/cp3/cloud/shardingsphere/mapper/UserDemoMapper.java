package com.cp3.cloud.shardingsphere.mapper;

import com.cp3.cloud.shardingsphere.entity.UserDemo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;


/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/18 19:20
 * @Version:1.0
 */
@Component
public interface UserDemoMapper extends BaseMapper<UserDemo> {

    @Select({"<script>",
            "select * from user_demo where 1=1 ",
            "<if test='id !=null'> and id = #{id} </if>",
            "<if test='phone !=null'> and phone = #{phone}</if>",
            " </script>"})
    List<UserDemo> findList(@Param("id") String id, @Param("phone") String phone);

    @Select("select * from user_demo where phone = #{phone} order by create_time")
    List<UserDemo> findUserByPhoneOderByTime(@Param("phone") String phone);

    @Update("update user_demo set user_name = #{userName} where id = #{id}")
    public Integer updateUserNameById(@Param("userName") String userName, @Param("id") String id);

    @Update("update user_demo set user_name = #{userName} where phone = #{phone}")
    public Integer updateUserNameByPhone(@Param("userName") String userName, @Param("phone") String phone);

    @Delete("delete from user_demo where phone = #{phone}")
    public Integer deleteUserByPhone(@Param("phone") String phone);
}
