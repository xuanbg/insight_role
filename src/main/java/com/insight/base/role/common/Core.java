package com.insight.base.role.common;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.entity.Role;
import com.insight.base.role.common.mapper.CoreMapper;
import com.insight.util.Generator;
import com.insight.util.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.insight.util.Generator.uuid;

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
            String id = Generator.uuid();
            role.setId(id);
            role.setTenantId(tenantId);
            role.setRemark(null);
            role.setCreator(creator);
            role.setCreatorId(creatorId);
            role.setCreatedTime(LocalDateTime.now());
            addRole(role);

            // 读取模板权限并写入角色功能授权
            List<FuncPermitDto> permits = mapper.getFuncPermits(templateId);
            setFuncPermits(id, permits);

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
     * @param id      角色ID
     * @param permits 角色功能授权集合
     */
    @Transactional
    public void setFuncPermits(String id, List<FuncPermitDto> permits) {
        mapper.removeFuncPermits(id);
        mapper.addFuncPermits(id, permits);
    }

    /**
     * 记录操作日志
     *
     * @param info     用户关键信息
     * @param type     操作类型
     * @param business 业务名称
     * @param id       业务ID
     * @param content  日志内容
     */
    @Async
    public void writeLog(LoginInfo info, OperateType type, String business, String id, Object content) {
        Log log = new Log();
        log.setId(uuid());
        log.setType(type);
        log.setBusiness(business);
        log.setBusinessId(id);
        log.setContent(content);
        log.setCreator(info.getUserName());
        log.setCreatorId(info.getUserId());
        log.setCreatedTime(LocalDateTime.now());

        mapper.addLog(log);
    }

    /**
     * 获取操作日志列表
     *
     * @param tenantId 租户ID
     * @param business 业务类型
     * @param key      查询关键词
     * @return 操作日志列表
     */
    public List<Log> getLogs(String tenantId, String business, String key) {
        return mapper.getLogs(tenantId, business, key);
    }

    /**
     * 获取操作日志
     *
     * @param id 日志ID
     * @return 操作日志
     */
    public Log getLog(String id) {
        return mapper.getLog(id);
    }
}
