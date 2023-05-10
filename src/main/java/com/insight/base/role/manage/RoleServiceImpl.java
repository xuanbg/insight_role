package com.insight.base.role.manage;

import com.github.pagehelper.PageHelper;
import com.insight.base.role.common.Core;
import com.insight.base.role.common.dto.AppListDto;
import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.dto.RoleMemberDto;
import com.insight.base.role.common.entity.Role;
import com.insight.base.role.common.mapper.RoleMapper;
import com.insight.utils.ReplyHelper;
import com.insight.utils.SnowflakeCreator;
import com.insight.utils.Util;
import com.insight.utils.pojo.auth.LoginInfo;
import com.insight.utils.pojo.base.BusinessException;
import com.insight.utils.pojo.base.Reply;
import com.insight.utils.pojo.base.Search;
import com.insight.utils.pojo.user.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 角色管理服务
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final SnowflakeCreator creator;
    private final RoleMapper mapper;
    private final Core core;

    /**
     * 构造方法
     *
     * @param creator 雪花算法ID生成器
     * @param mapper  RoleMapper
     * @param core    Core
     */
    public RoleServiceImpl(SnowflakeCreator creator, RoleMapper mapper, Core core) {
        this.creator = creator;
        this.mapper = mapper;
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
        try (var page = PageHelper.startPage(search.getPageNum(), search.getPageSize()).setOrderBy(search.getOrderBy())
                .doSelectPage(() -> mapper.getRoles(search))) {
            var total = page.getTotal();
            return total > 0 ? ReplyHelper.success(page.getResult(), total) : ReplyHelper.resultIsEmpty();
        }
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public Role getRole(Long id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        role.setFuncPermits(mapper.getFuncPermits(id));
        return role;
    }

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public List<MemberDto> getMembers(Long id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        return mapper.getMembers(id);
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
            throw new BusinessException("ID不存在,未读取数据");
        }

        try (var page = PageHelper.startPage(search.getPageNum(), search.getPageSize()).setOrderBy(search.getOrderBy())
                .doSelectPage(() -> mapper.getMemberUsers(search))) {
            var total = page.getTotal();
            return total > 0 ? ReplyHelper.success(page.getResult(), total) : ReplyHelper.resultIsEmpty();
        }
    }

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return Reply
     */
    @Override
    public List<FuncPermitDto> getFuncPermits(Long id) {
        Role role = mapper.getRole(id);
        if (id > 0 && role == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        return mapper.getFuncPermits(id);
    }

    /**
     * 获取角色可选应用列表
     *
     * @param tenantId 租户ID
     * @return Reply
     */
    @Override
    public List<AppListDto> getApps(Long tenantId) {
        return mapper.getApps(tenantId);
    }

    /**
     * 获取角色可选用户成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    @Override
    public List<RoleMemberDto> getMemberOfUser(Long tenantId, Long id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        return mapper.getMemberOfUser(tenantId, id);
    }

    /**
     * 获取角色可选用户组成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    @Override
    public List<RoleMemberDto> getMemberOfGroup(Long tenantId, Long id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        return mapper.getMemberOfGroup(tenantId, id);
    }

    /**
     * 获取角色可选职位成员
     *
     * @param tenantId 租户ID
     * @param id       角色ID
     * @return Reply
     */
    @Override
    public List<RoleMemberDto> getMemberOfTitle(Long tenantId, Long id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        return mapper.getMemberOfTitle(tenantId, id);
    }

    /**
     * 新增角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     * @return Reply
     */
    @Override
    @Transactional
    public Long newRole(LoginInfo info, Role dto) {
        if (info.getTenantId() != null && dto.getAppId() == null) {
            throw new BusinessException("appId不能为空");
        }

        Long id = creator.nextId(7);
        dto.setId(id);
        dto.setTenantId(info.getTenantId());
        dto.setCreator(info.getName());
        dto.setCreatorId(info.getId());
        dto.setCreatedTime(LocalDateTime.now());

        core.addRole(dto);
        return id;
    }

    /**
     * 编辑角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     */
    @Override
    @Transactional
    public void editRole(LoginInfo info, Role dto) {
        if (info.getTenantId() != null && dto.getAppId() == null) {
            throw new BusinessException("appId不能为空");
        }

        Long id = dto.getId();
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未更新数据");
        }

        if (dto.getAppId() == null) {
            dto.setAppId(role.getAppId());
        }

        if (Util.isEmpty(dto.getName())) {
            dto.setName(role.getName());
        }

        if (Util.isEmpty(dto.getRemark())) {
            dto.setRemark(role.getRemark());
        }

        core.editRole(dto);
    }

    /**
     * 删除角色
     *
     * @param info 用户关键信息
     * @param id   角色ID
     */
    @Override
    public void deleteRole(LoginInfo info, Long id) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未删除数据");
        }

        mapper.deleteRole(id);
    }

    /**
     * 添加角色成员
     *
     * @param info    用户关键信息
     * @param id      角色ID
     * @param members 角色成员集合
     */
    @Override
    public void addMembers(LoginInfo info, Long id, List<MemberDto> members) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未更新数据");
        }

        if (role.getTenantId() == null && role.getBuiltin()) {
            throw new BusinessException("角色模板不能添加角色成员");
        }

        core.addMembers(id, members);
    }

    /**
     * 移除角色成员
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param member 角色成员DTO
     */
    @Override
    public void removeMember(LoginInfo info, Long id, MemberDto member) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未删除数据");
        }

        mapper.removeMember(id, member);
    }

    /**
     * 设置角色权限
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param permit 角色权限
     */
    @Override
    public void setFuncPermit(LoginInfo info, Long id, FuncPermitDto permit) {
        Role role = mapper.getRole(id);
        if (role == null) {
            throw new BusinessException("ID不存在,未更新数据");
        }

        core.setFuncPermit(id, permit);
    }
}
