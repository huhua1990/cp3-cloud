package com.cp3.cloud.oauth.api;

import com.cp3.cloud.oauth.api.hystrix.DictionaryApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 数据字典API
 *
 * @author zuihou
 * @date 2019/07/26
 */
@FeignClient(name = "${cp3.feign.oauth-server:cp3-oauth-server}", path = "dictionary",
        qualifier = "dictionaryApi", fallback = DictionaryApiFallback.class)
public interface DictionaryApi {

    /**
     * 根据 code 查询字典
     *
     * @param codes 字典编码
     * @return 字典
     */
    @GetMapping("/findDictionaryItem")
    Map<Serializable, Object> findDictionaryItem(@RequestParam(value = "codes") Set<Serializable> codes);
}
