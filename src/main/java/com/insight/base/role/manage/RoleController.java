package com.insight.base.role.manage;

import com.insight.base.role.common.client.LogClient;
import com.insight.base.role.common.client.LogServiceClient;
import com.insight.base.role.common.dto.AppListDto;
import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.dto.RoleMemberDto;
import com.insight.base.role.common.entity.OperateType;
import com.insight.base.role.common.entity.Role;
import com.insight.utils.Json;
import com.insight.utils.pojo.auth.LoginInfo;
import com.insight.utils.pojo.base.BusinessException;
import com.insight.utils.pojo.base.Reply;
import com.insight.utils.pojo.base.Search;
import com.insight.utils.pojo.user.MemberDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 角色管理服务控制器
 */
@RestController
@RequestMapping("/base/role")
public class RoleController {
    private static final String BUSINESS = "Role";
    private final LogServiceClient client;
    private final RoleService service;

    /**
     * 构造方法
     *
     * @param client  Feign客户端
     * @param service 自动注入的Service
     */
    public RoleController(LogServiceClient client, RoleService service) {
        this.client = client;
        this.service = service;
    }

    /**
     * 查询角色列表
     *
     * @param loginInfo 用户关键信息
     * @param search    查询实体类
     * @return Reply
     */
    @GetMapping("/v1.0/roles")
    public Reply getRoles(@RequestHeader("loginInfo") String loginInfo, Search search) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        search.setTenantId(info.getTenantId());

        return service.getRoles(search);
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}")
    public Role getRole(@PathVariable Long id) {
        return service.getRole(id);
    }

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/members")
    public List<MemberDto> getMembers(@PathVariable Long id) {
        return service.getMembers(id);
    }

    /**
     * 查询角色成员用户
     *
     * @param id     角色ID
     * @param search 查询实体类
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/users")
    public Reply getMemberUsers(@PathVariable Long id, Search search) {
        search.setId(id);
        return service.getMemberUsers(search);
    }

    /**
     * 获取角色权限
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @param appId     应用ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/funcs")
    public List<FuncPermitDto> getFuncPermits(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id, @RequestParam(required = false) Long appId) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        return service.getFuncPermits(appId == null ? info.getAppId() : appId, id);
    }

    /**
     * 获取角色可选应用列表
     *
     * @param loginInfo 用户关键信息
     * @return Reply
     */
    @GetMapping("/v1.0/apps")
    public List<AppListDto> getApps(@RequestHeader("loginInfo") String loginInfo) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        return service.getApps(info.getTenantId());
    }

    /**
     * 获取角色可选用户成员
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/users/other")
    public List<RoleMemberDto> getMemberOfUser(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        return service.getMemberOfUser(info.getTenantId(), id);
    }

    /**
     * 获取角色可选用户组成员
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/groups/other")
    public List<RoleMemberDto> getMemberOfGroup(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        return service.getMemberOfGroup(info.getTenantId(), id);
    }

    /**
     * 获取角色可选职位成员
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/orgs/other")
    public List<RoleMemberDto> getMemberOfTitle(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        return service.getMemberOfTitle(info.getTenantId(), id);
    }

    /**
     * 新增角色
     *
     * @param loginInfo 用户关键信息
     * @param dto       角色DTO
     * @return Reply
     */
    @PostMapping("/v1.0/roles")
    public Long newRole(@RequestHeader("loginInfo") String loginInfo, @Valid @RequestBody Role dto) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        var id = service.newRole(info, dto);
        LogClient.writeLog(info, BUSINESS, OperateType.NEW, id, dto);
        return id;
    }

    /**
     * 编辑角色
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @param dto       角色DTO
     */
    @PutMapping("/v1.0/roles/{id}")
    public void editRole(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id, @Valid @RequestBody Role dto) {
        dto.setId(id);
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        var permits = service.editRole(info, dto);
        var map = new HashMap<String, Object>();
        map.put("before", permits);
        map.put("after", dto);
        LogClient.writeLog(info, BUSINESS, OperateType.EDIT, id, map);    }

    /**
     * 禁用角色
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     */
    @PutMapping("/v1.0/roles/{id}/disable")
    public void disableRole(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        service.updateRoleStatus(id, true);
        LogClient.writeLog(info, BUSINESS, OperateType.DISABLE, id, null);
    }

    /**
     * 启用角色
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     */
    @PutMapping("/v1.0/roles/{id}/enable")
    public void enableRole(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        service.updateRoleStatus(id, false);
        LogClient.writeLog(info, BUSINESS, OperateType.ENABLE, id, null);
    }

    /**
     * 删除角色
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     */
    @DeleteMapping("/v1.0/roles/{id}")
    public void deleteRole(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);

        service.deleteRole(info, id);
        LogClient.writeLog(info, BUSINESS, OperateType.DELETE, id, null);
    }

    /**
     * 添加角色成员
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @param members   角色成员集合
     */
    @PostMapping("/v1.0/roles/{id}/members")
    public void addMembers(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id, @RequestBody List<MemberDto> members) {
        if (members == null || members.isEmpty()) {
            throw new BusinessException("请选择需要添加的成员");
        }

        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        service.addMembers(info, id, members);
        LogClient.writeLog(info, BUSINESS, OperateType.EDIT, id, members);
    }

    /**
     * 移除角色成员
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @param member    角色成员DTO
     */
    @DeleteMapping("/v1.0/roles/{id}/members")
    public void removeMember(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id, @RequestBody MemberDto member) {
        if (member == null) {
            throw new BusinessException("请选择需要移除的成员");
        }

        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        service.removeMember(info, id, member);
        LogClient.writeLog(info, BUSINESS, OperateType.EDIT, id, member);
    }

    /**
     * 设置角色权限
     *
     * @param loginInfo 用户关键信息
     * @param id        角色ID
     * @param permit    角色权限
     */
    @PutMapping("/v1.0/roles/{id}/funcs")
    public void setFuncPermits(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id, @RequestBody FuncPermitDto permit) {
        if (permit == null) {
            throw new BusinessException("请选择需要授与的功能权限");
        }

        LoginInfo info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        service.setFuncPermit(info, id, permit);
        LogClient.writeLog(info, BUSINESS, OperateType.EDIT, id, permit);
    }

    /**
     * 查询日志
     *
     * @param loginInfo 用户登录信息
     * @param search    查询条件
     * @return 日志集合
     */
    @GetMapping("/v1.0/roles/{id}/logs")
    public Reply getAirportLogs(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id, Search search) {
        var info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        return client.getLogs(id, BUSINESS, search.getKeyword());
    }

    /**
     * 获取日志
     *
     * @param loginInfo 用户登录信息
     * @param id        日志ID
     * @return 日志VO
     */
    @GetMapping("/v1.0/roles/logs/{id}")
    public Reply getAirportLog(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        var info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        return client.getLog(id);
    }
}
