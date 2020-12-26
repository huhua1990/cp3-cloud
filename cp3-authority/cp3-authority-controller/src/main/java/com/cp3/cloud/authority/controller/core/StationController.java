package com.cp3.cloud.authority.controller.core;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.base.annotation.security.PreAuth;
import com.cp3.base.basic.R;
import com.cp3.base.basic.controller.SuperCacheController;
import com.cp3.base.basic.request.PageParams;
import com.cp3.base.model.RemoteData;
import com.cp3.cloud.authority.dto.core.StationPageQuery;
import com.cp3.cloud.authority.dto.core.StationSaveDTO;
import com.cp3.cloud.authority.dto.core.StationUpdateDTO;
import com.cp3.cloud.authority.entity.core.Station;
import com.cp3.cloud.authority.service.core.StationService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * 岗位
 * </p>
 *
 * @author zuihou
 * @date 2019-07-22
 */
@Slf4j
@RestController
@RequestMapping("/station")
@Api(value = "Station", tags = "岗位")
@PreAuth(replace = "authority:station:")
public class StationController extends SuperCacheController<StationService, Long, Station, StationPageQuery, StationSaveDTO, StationUpdateDTO> {

    @Override
    public void query(PageParams<StationPageQuery> params, IPage<Station> page, Long defSize) {
        baseService.findStationPage(page, params);
    }

    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list) {
        List<Station> stationList = list.stream().map((map) -> {
            Station item = new Station();
            item.setDescribe(map.getOrDefault("描述", ""));
            item.setName(map.getOrDefault("名称", ""));
            item.setOrg(new RemoteData<>(Convert.toLong(map.getOrDefault("组织", ""))));
            item.setState(Convert.toBool(map.getOrDefault("状态", "")));
            return item;
        }).collect(Collectors.toList());

        return R.success(baseService.saveBatch(stationList));
    }

}
