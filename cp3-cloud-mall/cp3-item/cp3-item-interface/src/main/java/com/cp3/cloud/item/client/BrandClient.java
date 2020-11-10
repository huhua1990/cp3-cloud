package com.cp3.cloud.item.client;

import com.cp3.cloud.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * Time: 2018-10-11 20:49
 * Feature:品牌FeignClient
 */
@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {
}
