package com.cp3.cloud.file.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.base.basic.service.SuperService;
import com.cp3.cloud.file.dto.AttachmentDTO;
import com.cp3.cloud.file.dto.AttachmentResultDTO;
import com.cp3.cloud.file.dto.FilePageReqDTO;
import com.cp3.cloud.file.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 业务接口
 * 附件
 * </p>
 *
 * @author zuihou
 * @date 2019-06-24
 */
public interface AttachmentService extends SuperService<Attachment> {
    /**
     * 上传附件
     *
     * @param file     文件
     * @param tenant   租户
     * @param id       附件id
     * @param bizType  业务类型
     * @param bizId    业务id
     * @param isSingle 是否单个文件
     * @return 附件
     */
    AttachmentDTO upload(MultipartFile file, String tenant, Long id, String bizType, String bizId, Boolean isSingle);

    /**
     * 删除附件
     *
     * @param ids id
     * @return 是否成功
     */
    boolean remove(List<Long> ids);

    /**
     * 根据业务id和业务类型删除附件
     *
     * @param bizId   业务id
     * @param bizType 业务类型
     * @return 是否成功
     */
    boolean removeByBizIdAndBizType(String bizId, String bizType);

    /**
     * 根据业务类型和业务id查询附件
     *
     * @param bizTypes 业务类型
     * @param bizIds   业务id
     * @return 附件
     */
    List<AttachmentResultDTO> find(String[] bizTypes, String[] bizIds);

    /**
     * 根据文件id下载附件
     *
     * @param request  请求
     * @param response 响应
     * @param ids      id
     * @throws Exception 异常
     */
    void download(HttpServletRequest request, HttpServletResponse response, Long[] ids) throws Exception;

    /**
     * 根据业务id和业务类型下载附件
     *
     * @param request  请求
     * @param response 响应
     * @param bizTypes 业务类型
     * @param bizIds   业务id
     * @throws Exception 异常
     */
    void downloadByBiz(HttpServletRequest request, HttpServletResponse response, String[] bizTypes, String[] bizIds) throws Exception;

    /**
     * 根据文件url下载附件
     *
     * @param request  请求
     * @param response 响应
     * @param url      链接
     * @param filename 文件名
     * @throws Exception 异常
     */
    void downloadByUrl(HttpServletRequest request, HttpServletResponse response, String url, String filename) throws Exception;

    /**
     * 查询附件分页数据，按权限
     *
     * @param page 分页参数
     * @param data 参数
     * @return 分页数据
     */
    IPage<Attachment> page(IPage<Attachment> page, FilePageReqDTO data);
}
