package com.cp3.cloud.authority.service.auth;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.cloud.authority.dto.auth.UserUpdatePasswordDTO;
import com.cp3.cloud.authority.entity.auth.User;
import com.cp3.cloud.base.service.SuperCacheService;
import com.cp3.cloud.database.mybatis.conditions.query.LbqWrapper;
import com.cp3.cloud.security.feign.UserQuery;
import com.cp3.cloud.security.model.SysUser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业务接口
 * 账号
 * </p>
 *
 * @author cp3
 * @date 2019-07-03
 */
public interface UserService extends SuperCacheService<User> {

    /**
     * 根据用户id 查询数据范围
     *
     * @param userId 用户id
     * @return
     */
    Map<String, Object> getDataScopeById(Long userId);

    /**
     * 根据角色id 和 账号或名称 查询角色关联的用户
     * <p>
     * 注意，该接口只返回 id，账号，姓名，手机，性别
     *
     * @param roleId  角色id
     * @param keyword 账号或名称
     * @return
     */
    List<User> findUserByRoleId(Long roleId, String keyword);

    /**
     * 检测账号是否存在
     *
     * @param account
     * @return
     */
    boolean check(String account);

    /**
     * 修改输错密码的次数
     *
     * @param id
     */
    void incrPasswordErrorNumById(Long id);

    /**
     * 根据账号查询用户
     *
     * @param account
     * @return
     */
    User getByAccount(String account);

    /**
     * 修改用户最后一次登录 时间
     *
     * @param account
     */
//    void updateLoginTime(String account);

    /**
     * 保存
     *
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * 重置密码
     *
     * @param ids
     * @return
     */
    boolean reset(UserUpdatePasswordDTO model);

    /**
     * 修改
     *
     * @param user
     * @return
     */
    User updateUser(User user);

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    boolean remove(List<Long> ids);

    /**
     * 数据权限 分页
     *
     * @param page
     * @param wrapper
     */
    IPage<User> findPage(IPage<User> page, LbqWrapper<User> wrapper);

    /**
     * 修改密码
     *
     * @param data
     * @return
     */
    Boolean updatePassword(UserUpdatePasswordDTO data);

    /**
     * 重置密码错误次数
     *
     * @param id
     * @return
     */
    int resetPassErrorNum(Long id);

    /**
     * 根据 id 查询用户
     *
     * @param ids
     * @return
     */
    Map<Serializable, Object> findUserByIds(Set<Serializable> ids);

    /**
     * 根据 id 查询用户名称
     *
     * @param ids
     * @return
     */
    Map<Serializable, Object> findUserNameByIds(Set<Serializable> ids);

    /**
     * 根据id 查询用户详情
     *
     * @param id
     * @param query
     * @return
     */
    SysUser getSysUserById(Long id, UserQuery query);

    /**
     * 查询所有用户的id
     *
     * @return
     */
    List<Long> findAllUserId();

    /**
     * 初始化用户
     *
     * @param user
     * @return
     */
    boolean initUser(User user);

    /**
     * 联合查询
     *
     * @param ids
     * @return
     */
    List<User> findUser(Set<Serializable> ids);

    List<User> findUserById(List<Long> ids);
}
