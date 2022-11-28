package com.insight.base.role.manage;

import com.insight.base.role.common.dto.AppListDto;
import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.dto.RoleMemberDto;
import com.insight.base.role.common.entity.Role;
import com.insight.utils.pojo.auth.LoginInfo;
import com.insight.utils.pojo.base.Reply;
import com.insight.utils.pojo.base.Search;
import com.insight.utils.pojo.user.MemberDto;

import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 角色管理服务接口
 */
public interface RoleService {

    /**
     * 查询角色列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    Reply getRoles(Search search);

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    Role getRole(Long id);

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return Reply
     */
    List<MemberDto> getMembers(Long id);

    /**
     * 查询角色成员用户
     *
     * @param search 查询实体类
     * @return Reply
     */
    Reply getMemberUsers(Search search);

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return Reply
     */
    List<FuncPermitDto> getFuncPermits(Long id);

    /**
     * 获取角色可选应用列表
     *
     * @param tenantId 租户ID
     * @return Reply
     */
    List<AppListDto> getApps(Long tenantId);

    /**
     * 获取角色可选用户成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    List<RoleMemberDto> getMemberOfUser(Long tenantId, Long id);

    /**
     * 获取角色可选用户组成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    List<RoleMemberDto> getMemberOfGroup(Long tenantId, Long id);

    /**
     * 获取角色可选职位成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    List<RoleMemberDto> getMemberOfTitle(Long tenantId, Long id);

    /**
     * 新增角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     * @return Reply
     */
    Long newRole(LoginInfo info, Role dto);

    /**
     * 编辑角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     */
    void editRole(LoginInfo info, Role dto);

    /**
     * 删除角色
     *
     * @param info 用户关键信息
     * @param id   角色ID
     */
    void deleteRole(LoginInfo info, Long id);

    /**
     * 添加角色成员
     *
     * @param info    用户关键信息
     * @param id      角色ID
     * @param members 角色成员集合
     */
    void addMembers(LoginInfo info, Long id, List<MemberDto> members);

    /**
     * 移除角色成员
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param member 角色成员DTO
     */
    void removeMember(LoginInfo info, Long id, MemberDto member);

    /**
     * 设置角色权限
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param permit 角色权限
     */
    void setFuncPermit(LoginInfo info, Long id, FuncPermitDto permit);

    /**
     * 获取日志列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    Reply getRoleLogs(Search search);

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    Reply getRoleLog(Long id);
}
