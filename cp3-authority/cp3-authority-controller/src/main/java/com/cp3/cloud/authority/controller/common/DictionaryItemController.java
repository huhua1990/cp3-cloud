package com.cp3.cloud.authority.controller.common;


import com.cp3.cloud.authority.dto.common.DictionaryItemSaveDTO;
import com.cp3.cloud.authority.dto.common.DictionaryItemUpdateDTO;
import com.cp3.cloud.authority.entity.common.DictionaryItem;
import com.cp3.cloud.authority.service.common.DictionaryItemService;
import com.cp3.cloud.base.controller.SuperCacheController;
import com.cp3.cloud.base.request.PageParams;
import com.cp3.cloud.database.mybatis.conditions.query.QueryWrap;
import com.cp3.cloud.security.annotation.PreAuth;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * 字典项
 * </p>
 *
 * @author cp3
 * @date 2019-07-22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/dictionaryItem")
@Api(value = "DictionaryItem", tags = "字典项")
@PreAuth(replace = "dict:")
public class DictionaryItemController extends SuperCacheController<DictionaryItemService, Long, DictionaryItem, DictionaryItem, DictionaryItemSaveDTO, DictionaryItemUpdateDTO> {
    @Override
    public QueryWrap<DictionaryItem> handlerWrapper(DictionaryItem model, PageParams<DictionaryItem> params) {
        QueryWrap<DictionaryItem> wrapper = super.handlerWrapper(model, params);
        wrapper.lambda().ignore(DictionaryItem::setDictionaryType)
                .eq(DictionaryItem::getDictionaryType, model.getDictionaryType());
        return wrapper;
    }

}
