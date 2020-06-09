package com.insight.base.role.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insight.base.role.common.Core;
import com.insight.base.role.common.client.LogClient;
import com.insight.base.role.common.client.LogServiceClient;
import com.insight.base.role.common.dto.*;
import com.insight.base.role.common.entity.Role;
import com.insight.base.role.common.mapper.RoleMapper;
import com.insight.utils.ReplyHelper;
import com.insight.utils.Util;
import com.insight.utils.pojo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 角色管理服务
 */
@Service
public class RoleServiceImpl implements RoleService {
    private static final String BUSINESS = "角色管理";
    private final RoleMapper mapper;
    private final LogServiceClient client;
    private final Core core;

    /**
     * 构造方法
     *
     * @param mapper RoleMapper
     * @param client LogServiceClient
     * @param core   Core
     */
    public RoleServiceImpl(RoleMapper mapper, LogServiceClient client, Core core) {
        this.mapper = mapper;
        this.client = client;
        this.core = core;
    }

    /**
     * 查询角色列表
     *
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    @Override
    public Reply getRoles(String tenantId, String keyword, int page, int size) {
        PageHelper.startPage(page, size);
        List<RoleListDto> groups = mapper.getRoles(tenantId, keyword);
        PageInfo<RoleListDto> pageInfo = new PageInfo<>(groups);

        return ReplyHelper.success(groups, pageInfo.getTotal());
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public Reply getRole(String id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        return ReplyHelper.success(role);
    }

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public Reply getMembers(String id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        List<MemberDto> members = mapper.getMembers(id);
        return ReplyHelper.success(members);
    }

    /**
     * 查询角色成员用户
     *
     * @param id      角色ID
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @Override
    public Reply getMemberUsers(String id, String keyword, int page, int size) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        PageHelper.startPage(page, size);
        List<MemberUserDto> users = mapper.getMemberUsers(id, keyword);
        PageInfo<MemberUserDto> pageInfo = new PageInfo<>(users);

        return ReplyHelper.success(users, pageInfo.getTotal());
    }

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public Reply getFuncPermits(String id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        List<FuncPermitDto> permits = mapper.getFuncPermits(id);
        return ReplyHelper.success(permits);
    }

    /**
     * 获取角色可选应用列表
     *
     * @param tenantId 租户ID
     * @return Reply
     */
    @Override
    public Reply getApps(String tenantId) {
        List<AppListDto> apps = mapper.getApps(tenantId);

        return ReplyHelper.success(apps);
    }

    /**
     * 获取角色可选用户成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    @Override
    public Reply getMemberOfUser(String tenantId, String id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        List<RoleMemberDto> members = mapper.getMemberOfUser(tenantId, id);
        return ReplyHelper.success(members);
    }

    /**
     * 获取角色可选用户组成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    @Override
    public Reply getMemberOfGroup(String tenantId, String id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        List<RoleMemberDto> members = mapper.getMemberOfGroup(tenantId, id);
        return ReplyHelper.success(members);
    }

    /**
     * 获取角色可选职位成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    @Override
    public Reply getMemberOfTitle(String tenantId, String id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        List<RoleMemberDto> members = mapper.getMemberOfTitle(tenantId, id);
        return ReplyHelper.success(members);
    }

    /**
     * 新增角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     * @return Reply
     */
    @Override
    public Reply newRole(LoginInfo info, Role dto) {
        String id = Util.uuid();
        dto.setId(id);
        dto.setTenantId(info.getTenantId());
        dto.setBuiltin(info.getTenantId() == null && !"9dd99dd9e6df467a8207d05ea5581125".equals(dto.getAppId()));
        dto.setCreator(info.getUserName());
        dto.setCreatorId(info.getUserId());
        dto.setCreatedTime(LocalDateTime.now());

        core.addRole(dto);
        LogClient.writeLog(info, BUSINESS, OperateType.INSERT, id, dto);

        return ReplyHelper.created(id);
    }

    /**
     * 编辑角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     * @return Reply
     */
    @Override
    public Reply editRole(LoginInfo info, Role dto) {
        String id = dto.getId();
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未更新数据");
        }

        mapper.updateRole(dto);
        LogClient.writeLog(info, BUSINESS, OperateType.UPDATE, id, dto);

        return ReplyHelper.success();
    }

    /**
     * 删除角色
     *
     * @param info 用户关键信息
     * @param id   角色ID
     * @return Reply
     */
    @Override
    public Reply deleteRole(LoginInfo info, String id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未删除数据");
        }

        mapper.deleteRole(id);
        LogClient.writeLog(info, BUSINESS, OperateType.DELETE, id, role);

        return ReplyHelper.success();
    }

    /**
     * 添加角色成员
     *
     * @param info    用户关键信息
     * @param id      角色ID
     * @param members 角色成员集合
     * @return Reply
     */
    @Override
    public Reply addMembers(LoginInfo info, String id, List<MemberDto> members) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未更新数据");
        }

        if (role.getTenantId() == null && role.getBuiltin()) {
            return ReplyHelper.fail("角色模板不能添加角色成员");
        }

        core.addMembers(id, members);
        LogClient.writeLog(info, BUSINESS, OperateType.INSERT, id, members);

        return ReplyHelper.success();
    }

    /**
     * 移除角色成员
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param member 角色成员DTO
     * @return Reply
     */
    @Override
    public Reply removeMember(LoginInfo info, String id, MemberDto member) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未删除数据");
        }

        mapper.removeMember(id, member);
        LogClient.writeLog(info, BUSINESS, OperateType.DELETE, id, member);

        return ReplyHelper.success();
    }

    /**
     * 设置角色权限
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param permit 角色权限
     * @return Reply
     */
    @Override
    public Reply setFuncPermit(LoginInfo info, String id, FuncPermitDto permit) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未更新数据");
        }

        core.setFuncPermit(id, permit);
        LogClient.writeLog(info, BUSINESS, OperateType.DELETE, id, permit);

        return ReplyHelper.success();
    }

    /**
     * 获取日志列表
     *
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @Override
    public Reply getRoleLogs(String keyword, int page, int size) {
        return client.getLogs(BUSINESS, keyword, page, size);
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @Override
    public Reply getRoleLog(String id) {
        return client.getLog(id);
    }
}
