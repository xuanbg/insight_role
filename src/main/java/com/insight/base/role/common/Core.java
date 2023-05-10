package com.insight.base.role.common;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.dto.RoleDto;
import com.insight.base.role.common.entity.Role;
import com.insight.base.role.common.mapper.CoreMapper;
import com.insight.utils.SnowflakeCreator;
import com.insight.utils.Util;
import com.insight.utils.pojo.user.MemberDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019/11/30
 * @remark 租户核心类
 */
@Component
public class Core {
    private final SnowflakeCreator creator;
    private final CoreMapper mapper;

    /**
     * 构造方法
     *
     * @param creator 雪花算法ID生成器
     * @param mapper  TenantMapper
     */
    public Core(SnowflakeCreator creator, CoreMapper mapper) {
        this.creator = creator;
        this.mapper = mapper;
    }

    /**
     * 从角色模板新增角色
     *
     * @param dto 角色DTO
     */
    @Transactional
    public void addRoleFromTemplate(RoleDto dto) {
        List<Role> roles = mapper.getTemplates(dto.getAppId());
        if (roles == null || roles.isEmpty()) {
            return;
        }

        // 构造角色数据
        List<MemberDto> members = dto.getMembers();
        for (Role role : roles) {
            Long templateId = role.getId();
            Long id = creator.nextId(7);
            role.setId(id);
            role.setTenantId(dto.getTenantId());
            role.setRemark(null);
            role.setCreator(dto.getCreator());
            role.setCreatorId(dto.getCreatorId());
            role.setCreatedTime(LocalDateTime.now());
            addRole(role);

            // 读取模板权限并写入角色功能授权
            List<FuncPermitDto> permits = mapper.getFuncPermits(templateId);
            mapper.addFuncPermits(id, permits);

            // 写入角色成员
            if (members == null || members.isEmpty()) {
                continue;
            }

            addMembers(id, members);
        }
    }

    /**
     * 新增角色
     *
     * @param role 角色DTO
     */
    public void addRole(Role role) {
        mapper.addRole(role);
        var ids = role.getFuncPermits().stream().filter(FuncPermitDto::hasPermit).map(FuncPermitDto::getId).toList();
        if (Util.isNotEmpty(ids)) {
            mapper.removeDataPermits(role.getId());
            mapper.addDataPermits(role.getId(), ids);
        }
    }

    /**
     * 编辑角色
     *
     * @param role 角色DTO
     */
    public void editRole(Role role) {
        mapper.updateRole(role);
        var ids = role.getFuncPermits().stream().filter(FuncPermitDto::hasPermit).map(FuncPermitDto::getId).toList();
        if (Util.isNotEmpty(ids)) {
            mapper.removeDataPermits(role.getId());
            mapper.addDataPermits(role.getId(), ids);
        }
    }

    /**
     * 新增角色成员
     *
     * @param id      角色ID
     * @param members 角色成员集合
     */
    public void addMembers(Long id, List<MemberDto> members) {
        mapper.addMembers(id, members);
    }

    /**
     * 添加角色功能授权
     *
     * @param id     角色ID
     * @param permit 角色功能授权DTO
     */
    @Transactional
    public void setFuncPermit(Long id, FuncPermitDto permit) {
        if (permit.getPermit() == null) {
            mapper.removeFuncPermit(id, permit.getId());
            return;
        }

        FuncPermitDto data = mapper.getFuncPermit(id, permit.getId());
        if (data == null) {
            mapper.addFuncPermit(id, permit.getId(), permit.getPermit());
        } else {
            mapper.setFuncPermit(id, permit.getId(), permit.getPermit());
        }
    }
}
