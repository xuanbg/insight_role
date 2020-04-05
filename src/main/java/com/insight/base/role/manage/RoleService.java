package com.insight.base.role.manage;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.entity.Role;
import com.insight.utils.pojo.LoginInfo;
import com.insight.utils.pojo.MemberDto;
import com.insight.utils.pojo.Reply;

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
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    Reply getRoles(String tenantId, String keyword, int page, int size);

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    Reply getRole(String id);

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return Reply
     */
    Reply getMembers(String id);

    /**
     * 查询角色成员用户
     *
     * @param id      角色ID
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    Reply getMemberUsers(String id, String keyword, int page, int size);

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return Reply
     */
    Reply getFuncPermits(String id);

    /**
     * 获取角色可选应用列表
     *
     * @param tenantId 租户ID
     * @return Reply
     */
    Reply getApps(String tenantId);

    /**
     * 获取角色可选用户成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    Reply getMemberOfUser(String tenantId, String id);

    /**
     * 获取角色可选用户组成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    Reply getMemberOfGroup(String tenantId, String id);

    /**
     * 获取角色可选职位成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    Reply getMemberOfTitle(String tenantId, String id);

    /**
     * 新增角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     * @return Reply
     */
    Reply newRole(LoginInfo info, Role dto);

    /**
     * 编辑角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     * @return Reply
     */
    Reply editRole(LoginInfo info, Role dto);

    /**
     * 删除角色
     *
     * @param info 用户关键信息
     * @param id   角色ID
     * @return Reply
     */
    Reply deleteRole(LoginInfo info, String id);

    /**
     * 添加角色成员
     *
     * @param info    用户关键信息
     * @param id      角色ID
     * @param members 角色成员集合
     * @return Reply
     */
    Reply addMembers(LoginInfo info, String id, List<MemberDto> members);

    /**
     * 移除角色成员
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param member 角色成员DTO
     * @return Reply
     */
    Reply removeMember(LoginInfo info, String id, MemberDto member);

    /**
     * 设置角色权限
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param permit 角色权限
     * @return Reply
     */
    Reply setFuncPermit(LoginInfo info, String id, FuncPermitDto permit);

    /**
     * 获取日志列表
     *
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    Reply getRoleLogs(String tenantId, String keyword, int page, int size);

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    Reply getRoleLog(String id);
}
