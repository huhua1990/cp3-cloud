package com.cp3.cloud.tenant.service;

import com.cp3.cloud.authority.dto.auth.UserUpdatePasswordDTO;
import com.cp3.cloud.base.service.SuperService;
import com.cp3.cloud.tenant.dto.GlobalUserSaveDTO;
import com.cp3.cloud.tenant.dto.GlobalUserUpdateDTO;
import com.cp3.cloud.tenant.entity.GlobalUser;

/**
 * <p>
 * 业务接口
 * 全局账号
 * </p>
 *
 * @author cp3
 * @date 2019-10-25
 */
public interface GlobalUserService extends SuperService<GlobalUser> {

    /**
     * 检测账号是否可用
     *
     * @param account
     * @return
     */
    Boolean check(String account);

    /**
     * 新建用户
     *
     * @param data
     * @return
     */
    GlobalUser save(GlobalUserSaveDTO data);


    /**
     * 修改
     *
     * @param data
     * @return
     */
    GlobalUser update(GlobalUserUpdateDTO data);

    /**
     * 修改密码
     *
     * @param model
     * @return
     */
    Boolean updatePassword(UserUpdatePasswordDTO model);
}
