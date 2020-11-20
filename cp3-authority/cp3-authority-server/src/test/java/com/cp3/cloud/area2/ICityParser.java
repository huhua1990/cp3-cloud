package com.cp3.cloud.area2;

import com.cp3.cloud.authority.entity.common.Area;

import java.util.List;


public interface ICityParser {

    /**
     * 解析得到省市区数据
     *
     * @param url 请求url
     * @return 城市
     */
    List<Area> parseProvinces(String url);
}
