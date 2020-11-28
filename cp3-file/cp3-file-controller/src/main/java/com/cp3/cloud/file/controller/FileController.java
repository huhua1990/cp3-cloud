package com.cp3.cloud.file.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.cloud.base.R;
import com.cp3.cloud.base.controller.SuperController;
import com.cp3.cloud.base.request.PageParams;
import com.cp3.cloud.file.dto.FilePageReqDTO;
import com.cp3.cloud.file.dto.FileUpdateDTO;
import com.cp3.cloud.file.dto.FolderDTO;
import com.cp3.cloud.file.dto.FolderSaveDTO;
import com.cp3.cloud.file.entity.File;
import com.cp3.cloud.file.manager.FileRestManager;
import com.cp3.cloud.file.service.FileService;
import com.cp3.cloud.log.annotation.SysLog;
import com.cp3.cloud.utils.BeanPlusUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 文件表 前端控制器
 * </p>
 *
 * @author cp3
 * @since 2019-04-29
 */
@Validated
@RestController
@RequestMapping("/file")
@Slf4j
@Api(value = "文件表", tags = "文件表")
public class FileController extends SuperController<FileService, Long, File, FilePageReqDTO, FolderSaveDTO, FileUpdateDTO> {
    @Autowired
    private FileRestManager fileRestManager;

    @Override
    public void query(PageParams<FilePageReqDTO> params, IPage<File> page, Long defSize) {
        fileRestManager.page(page, params.getModel());
    }

    @Override
    public R<File> handlerSave(FolderSaveDTO model) {
        FolderDTO folder = baseService.saveFolder(model);
        return success(BeanPlusUtil.toBean(folder, File.class));
    }

    /**
     * 上传文件
     *
     * @param
     * @return
     * @author cp3
     * @date 2019-05-06 16:28
     */
    @ApiOperation(value = "上传文件", notes = "上传文件 ")
    @ApiResponses({
            @ApiResponse(code = 60102, message = "文件夹为空"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "folderId", value = "文件夹id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "附件", dataType = "MultipartFile", allowMultiple = true, required = true),
    })
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @SysLog("上传文件")
    public R<File> upload(
            @NotNull(message = "文件夹不能为空")
            @RequestParam(value = "folderId") Long folderId,
            @RequestParam(value = "file") MultipartFile simpleFile) {
        //1，先将文件存在本地,并且生成文件名
        log.info("contentType={}, name={} , sfname={}", simpleFile.getContentType(), simpleFile.getName(), simpleFile.getOriginalFilename());
        // 忽略路径字段,只处理文件类型
        if (simpleFile.getContentType() == null) {
            return fail("文件为空");
        }

        File file = baseService.upload(simpleFile, folderId);

        return success(file);
    }


    @Override
    public R<File> handlerUpdate(FileUpdateDTO fileUpdateDTO) {
        // 判断文件名是否有 后缀
        if (StrUtil.isNotEmpty(fileUpdateDTO.getSubmittedFileName())) {
            File oldFile = baseService.getById(fileUpdateDTO.getId());
            if (oldFile.getExt() != null && !fileUpdateDTO.getSubmittedFileName().endsWith(oldFile.getExt())) {
                fileUpdateDTO.setSubmittedFileName(fileUpdateDTO.getSubmittedFileName() + "." + oldFile.getExt());
            }
        }
        File file = BeanPlusUtil.toBean(fileUpdateDTO, File.class);

        baseService.updateById(file);
        return success(file);
    }

    @Override
    public R<Boolean> handlerDelete(List<Long> ids) {
        Long userId = getUserId();
        return success(baseService.removeList(userId, ids));
    }

    /**
     * 下载一个文件或多个文件打包下载
     *
     * @param ids
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "下载一个文件或多个文件打包下载", notes = "下载一个文件或多个文件打包下载")
    @GetMapping(value = "/download", produces = "application/octet-stream")
    @SysLog("下载文件")
    public void download(
            @ApiParam(name = "ids[]", value = "文件id 数组")
            @RequestParam(value = "ids[]") Long[] ids,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileRestManager.download(request, response, ids, null);
    }

}
