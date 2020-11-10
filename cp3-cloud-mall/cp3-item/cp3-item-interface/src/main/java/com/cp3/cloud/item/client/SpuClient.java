package com.cp3.cloud.item.client;

import com.cp3.cloud.item.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * Time: 2018-10-11 20:50
 * Feature:spu FeignClient
 */
@FeignClient(value = "item-service")
public interface SpuClient extends SpuApi {
}
