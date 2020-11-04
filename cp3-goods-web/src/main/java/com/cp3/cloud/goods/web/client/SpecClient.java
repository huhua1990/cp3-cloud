package com.cp3.cloud.goods.web.client;

import com.cp3.cloud.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * Time: 2018-10-17 19:01
 * Feature:spec FeignClient
 */
@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {
}
