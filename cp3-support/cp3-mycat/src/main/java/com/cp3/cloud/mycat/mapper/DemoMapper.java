package com.cp3.cloud.mycat.mapper;

import com.cp3.cloud.mycat.entity.UserDemo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/23 18:33
 * @Version:1.0
 */
@Component
public interface DemoMapper extends Mapper<UserDemo> {
    @Select("select * from t_user")
    List<UserDemo> findList();
}
