package com.cp3.cloud.mycat.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/23 15:16
 * @Version:1.0
 */
@Table(name = "t_user")
@Data
public class UserDemo {
    @Id
    private Integer id;
    private String name;
}
