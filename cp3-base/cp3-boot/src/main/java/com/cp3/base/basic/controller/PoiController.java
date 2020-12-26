package com.cp3.base.basic.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.base.annotation.log.SysLog;
import com.cp3.base.annotation.security.PreAuth;
import com.cp3.base.basic.R;
import com.cp3.base.basic.request.PageParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入导出
 *
 * @param <Entity>    实体
 * @param <PageQuery> 分页查询参数
 * @author zuihou
 * @date 2020年03月07日22:02:06
 */
public interface PoiController<Entity, PageQuery> extends PageController<Entity, PageQuery> {

    /**
     * 导出Excel
     *
     * @param params   参数
     * @param request  请求
     * @param response 响应
     */
    @ApiOperation(value = "导出Excel")
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = "application/octet-stream")
    @SysLog("'导出Excel:'.concat(#params.extra[" + NormalExcelConstants.FILE_NAME + "]?:'')")
    @PreAuth("hasAnyPermission('{}export')")
    default void exportExcel(@RequestBody @Validated PageParams<PageQuery> params, HttpServletRequest request, HttpServletResponse response) {
        IPage<Entity> page = params.buildPage();
        ExportParams exportParams = getExportParams(params, page);

        Map<String, Object> map = new HashMap<>(7);
        map.put(NormalExcelConstants.DATA_LIST, page.getRecords());
        map.put(NormalExcelConstants.CLASS, getEntityClass());
        map.put(NormalExcelConstants.PARAMS, exportParams);
        Object fileName = params.getExtra().getOrDefault(NormalExcelConstants.FILE_NAME, "临时文件");
        map.put(NormalExcelConstants.FILE_NAME, fileName);
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     * 预览Excel
     *
     * @param params 预览参数
     * @return 预览html
     */
    @ApiOperation(value = "预览Excel")
    @SysLog("'预览Excel:' + (#params.extra[" + NormalExcelConstants.FILE_NAME + "]?:'')")
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @PreAuth("hasAnyPermission('{}export')")
    default R<String> preview(@RequestBody @Validated PageParams<PageQuery> params) {
        IPage<Entity> page = params.buildPage();
        ExportParams exportParams = getExportParams(params, page);

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, getEntityClass(), page.getRecords());
        return success(ExcelXorHtmlUtil.excelToHtml(new ExcelToHtmlParams(workbook)));
    }

    /**
     * 使用自动生成的实体+注解方式导入 对RemoteData 类型的字段不支持，
     * 建议自建实体使用
     *
     * @param simpleFile 上传文件
     * @param request    请求
     * @param response   响应
     * @return 是否导入成功
     * @throws Exception 异常
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping(value = "/import")
    @SysLog(value = "'导入Excel:' + #simpleFile?.originalFilename", request = false)
    @PreAuth("hasAnyPermission('{}import')")
    default R<Boolean> importExcel(@RequestParam(value = "file") MultipartFile simpleFile, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        ImportParams params = new ImportParams();

        params.setTitleRows(StrUtil.isEmpty(request.getParameter("titleRows")) ? 0 : Convert.toInt(request.getParameter("titleRows")));
        params.setHeadRows(StrUtil.isEmpty(request.getParameter("headRows")) ? 1 : Convert.toInt(request.getParameter("headRows")));
        List<Map<String, String>> list = ExcelImportUtil.importExcel(simpleFile.getInputStream(), Map.class, params);

        if (list != null && !list.isEmpty()) {
            return handlerImport(list);
        }
        return validFail("导入Excel无有效数据！");
    }

    /**
     * 转换后保存
     *
     * @param list 集合
     * @return 是否成功
     */
    default R<Boolean> handlerImport(List<Map<String, String>> list) {
        return R.successDef(null, "请在子类Controller重写导入方法，实现导入逻辑");
    }

    /**
     * 构建导出参数
     *
     * @param params 分页参数
     * @param page   分页
     * @return 导出参数
     */
    default ExportParams getExportParams(PageParams<PageQuery> params, IPage<Entity> page) {
        query(params, page, params.getSize() == -1 ? Convert.toLong(Integer.MAX_VALUE) : params.getSize());

        Object title = params.getExtra().get("title");
        Object type = params.getExtra().getOrDefault("type", ExcelType.XSSF.name());
        Object sheetName = params.getExtra().getOrDefault("sheetName", "SheetName");

        ExcelType excelType = ExcelType.XSSF.name().equals(type) ? ExcelType.XSSF : ExcelType.HSSF;
        return new ExportParams(String.valueOf(title), sheetName.toString(), excelType);
    }
}
