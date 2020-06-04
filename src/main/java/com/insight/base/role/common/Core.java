package com.insight.base.role.common;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.dto.RoleDto;
import com.insight.base.role.common.entity.Role;
import com.insight.base.role.common.mapper.CoreMapper;
import com.insight.utils.Util;
import com.insight.utils.pojo.MemberDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CoreMapper mapper;

    /**
     * 构造方法
     *
     * @param mapper TenantMapper
     */
    public Core(CoreMapper mapper) {
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
        String tenantId = dto.getTenantId();
        String creator = dto.getCreator();
        String creatorId = dto.getCreatorId();
        List<MemberDto> members = dto.getMembers();
        for (Role role : roles) {
            String templateId = role.getId();
            String id = Util.uuid();
            role.setId(id);
            role.setTenantId(tenantId);
            role.setRemark(null);
            role.setCreator(creator);
            role.setCreatorId(creatorId);
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
    }

    /**
     * 新增角色成员
     *
     * @param id      角色ID
     * @param members 角色成员集合
     */
    public void addMembers(String id, List<MemberDto> members) {
        mapper.addMembers(id, members);
    }

    /**
     * 添加角色功能授权
     *
     * @param id     角色ID
     * @param permit 角色功能授权DTO
     */
    @Transactional
    public void setFuncPermit(String id, FuncPermitDto permit) {
        if (permit.getPermit() == null) {
            mapper.removeFuncPermit(id, permit.getId());
            return;
        }

        FuncPermitDto data = mapper.getFuncPermit(id, permit.getId());
        if (data == null){
            mapper.addFuncPermit(id, permit.getId(), permit.getPermit());
        }else {
            mapper.setFuncPermit(id, permit.getId(), permit.getPermit());
        }
    }
}
