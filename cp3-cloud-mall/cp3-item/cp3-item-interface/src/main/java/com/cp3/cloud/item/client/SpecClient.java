package com.cp3.cloud.item.client;

import com.cp3.cloud.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * Time: 2018-10-11 20:50
 * Feature:特有属性FeignClient
 */
@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {
}
