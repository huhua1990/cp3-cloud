package com.cp3.cloud.shardingsphere.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/18 18:20
 * @Version:1.0
 */
@Data
@Table(name="user_demo")
public class UserDemo implements Serializable {

    @Id
    private String id;
    private String userName; // 昵称
    private String realName; // 真实姓名
    private String identify; // 身份证号码
    private String phone; // 手机号码
    private Date createTime;
    private Date updateTime;
}
