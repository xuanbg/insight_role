package com.insight.base.role.manage;

import com.github.pagehelper.PageHelper;
import com.insight.base.role.common.Core;
import com.insight.base.role.common.client.LogClient;
import com.insight.base.role.common.client.LogServiceClient;
import com.insight.base.role.common.dto.AppListDto;
import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.dto.RoleMemberDto;
import com.insight.base.role.common.entity.Role;
import com.insight.base.role.common.mapper.RoleMapper;
import com.insight.utils.ReplyHelper;
import com.insight.utils.SnowflakeCreator;
import com.insight.utils.pojo.OperateType;
import com.insight.utils.pojo.auth.LoginInfo;
import com.insight.utils.pojo.base.Reply;
import com.insight.utils.pojo.base.Search;
import com.insight.utils.pojo.user.MemberDto;
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
    private final SnowflakeCreator creator;
    private final RoleMapper mapper;
    private final LogServiceClient client;
    private final Core core;

    /**
     * 构造方法
     *
     * @param creator 雪花算法ID生成器
     * @param mapper  RoleMapper
     * @param client  LogServiceClient
     * @param core    Core
     */
    public RoleServiceImpl(SnowflakeCreator creator, RoleMapper mapper, LogServiceClient client, Core core) {
        this.creator = creator;
        this.mapper = mapper;
        this.client = client;
        this.core = core;
    }

    /**
     * 查询角色列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    @Override
    public Reply getRoles(Search search) {
        var page = PageHelper.startPage(search.getPageNum(), search.getPageSize())
                .setOrderBy(search.getOrderBy()).doSelectPage(() -> mapper.getRoles(search));

        var total = page.getTotal();
        return total > 0 ? ReplyHelper.success(page.getResult(), total) : ReplyHelper.resultIsEmpty();
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public Reply getRole(Long id) {
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
    public Reply getMembers(Long id) {
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
     * @param search 查询实体类
     * @return Reply
     */
    @Override
    public Reply getMemberUsers(Search search) {
        Role role = mapper.getRole(search.getId());
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        var page = PageHelper.startPage(search.getPageNum(), search.getPageSize())
                .setOrderBy(search.getOrderBy()).doSelectPage(() -> mapper.getMemberUsers(search));

        var total = page.getTotal();
        return total > 0 ? ReplyHelper.success(page.getResult(), total) : ReplyHelper.resultIsEmpty();
    }

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public Reply getFuncPermits(Long id) {
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
    public Reply getApps(Long tenantId) {
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
    public Reply getMemberOfUser(Long tenantId, Long id) {
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
    public Reply getMemberOfGroup(Long tenantId, Long id) {
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
    public Reply getMemberOfTitle(Long tenantId, Long id) {
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
        if (info.getTenantId() != null && dto.getAppId() == null){
            return ReplyHelper.invalidParam("appId不能为空");
        }

        Long id = creator.nextId(7);
        dto.setId(id);
        dto.setTenantId(info.getTenantId());
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
        if (info.getTenantId() != null && dto.getAppId() == null){
            return ReplyHelper.invalidParam("appId不能为空");
        }

        Long id = dto.getId();
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
    public Reply deleteRole(LoginInfo info, Long id) {
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
    public Reply addMembers(LoginInfo info, Long id, List<MemberDto> members) {
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
    public Reply removeMember(LoginInfo info, Long id, MemberDto member) {
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
    public Reply setFuncPermit(LoginInfo info, Long id, FuncPermitDto permit) {
        Role role = mapper.getRole(id);
        if (role == null) {
            return ReplyHelper.fail("ID不存在,未更新数据");
        }

        core.setFuncPermit(id, permit);
        LogClient.writeLog(info, BUSINESS, OperateType.UPDATE, id, permit);

        return ReplyHelper.success();
    }

    /**
     * 获取日志列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    @Override
    public Reply getRoleLogs(Search search) {
        return client.getLogs(BUSINESS, search.getKeyword(), search.getPageNum(), search.getPageSize());
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @Override
    public Reply getRoleLog(Long id) {
        return client.getLog(id);
    }
}
