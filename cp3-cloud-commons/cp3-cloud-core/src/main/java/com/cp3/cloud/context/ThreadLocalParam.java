package com.cp3.cloud.context;

import lombok.Data;

import java.io.Serializable;

/**
 * 线程变量封装的参数
 *
 * @author cp3
 * @date 2020/11/1 2:10 下午
 */
@Data
public class ThreadLocalParam implements Serializable {
    private Boolean boot;
    private String tenant;
    private Long userid;
    private String name;
    private String account;
}
