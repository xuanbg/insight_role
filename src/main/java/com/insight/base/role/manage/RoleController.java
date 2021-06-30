package com.insight.base.role.manage;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.entity.Role;
import com.insight.utils.Json;
import com.insight.utils.ReplyHelper;
import com.insight.utils.pojo.LoginInfo;
import com.insight.utils.pojo.MemberDto;
import com.insight.utils.pojo.Reply;
import com.insight.utils.pojo.SearchDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 角色管理服务控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/base/role")
public class RoleController {
    private final RoleService service;

    /**
     * 构造方法
     *
     * @param service 自动注入的Service
     */
    public RoleController(RoleService service) {
        this.service = service;
    }

    /**
     * 查询角色列表
     *
     * @param info   用户关键信息
     * @param search 查询实体类
     * @return Reply
     */
    @GetMapping("/v1.0/roles")
    public Reply getRoles(@RequestHeader("loginInfo") String info, SearchDto search) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        search.setTenantId(loginInfo.getTenantId());

        return service.getRoles(search);
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}")
    public Reply getRole(@PathVariable Long id) {
        if (id == null) {
            return ReplyHelper.invalidParam();
        }

        return service.getRole(id);
    }

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/members")
    public Reply getMembers(@PathVariable Long id) {
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
    public Reply getMemberUsers(@PathVariable Long id, SearchDto search) {
        return service.getMemberUsers(id, search);
    }

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/funcs")
    public Reply getFuncPermits(@PathVariable Long id) {
        return service.getFuncPermits(id);
    }

    /**
     * 获取角色可选应用列表
     *
     * @param info 用户关键信息
     * @return Reply
     */
    @GetMapping("/v1.0/apps")
    public Reply getApps(@RequestHeader("loginInfo") String info) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getApps(loginInfo.getTenantId());
    }

    /**
     * 获取角色可选用户成员
     *
     * @param info 用户关键信息
     * @param id   角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/users/other")
    public Reply getMemberOfUser(@RequestHeader("loginInfo") String info, @PathVariable Long id) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getMemberOfUser(loginInfo.getTenantId(), id);
    }

    /**
     * 获取角色可选用户组成员
     *
     * @param info 用户关键信息
     * @param id   角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/groups/other")
    public Reply getMemberOfGroup(@RequestHeader("loginInfo") String info, @PathVariable Long id) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getMemberOfGroup(loginInfo.getTenantId(), id);
    }

    /**
     * 获取角色可选职位成员
     *
     * @param info 用户关键信息
     * @param id   角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/orgs/other")
    public Reply getMemberOfTitle(@RequestHeader("loginInfo") String info, @PathVariable Long id) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getMemberOfTitle(loginInfo.getTenantId(), id);
    }

    /**
     * 新增角色
     *
     * @param info 用户关键信息
     * @param dto  角色DTO
     * @return Reply
     */
    @PostMapping("/v1.0/roles")
    public Reply newRole(@RequestHeader("loginInfo") String info, @Valid @RequestBody Role dto) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.newRole(loginInfo, dto);
    }

    /**
     * 编辑角色
     *
     * @param info 用户关键信息
     * @param id   角色ID
     * @param dto  角色DTO
     * @return Reply
     */
    @PutMapping("/v1.0/roles/{id}")
    public Reply editRole(@RequestHeader("loginInfo") String info, @PathVariable Long id, @Valid @RequestBody Role dto) {
        dto.setId(id);
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.editRole(loginInfo, dto);
    }

    /**
     * 删除角色
     *
     * @param info 用户关键信息
     * @param id   角色ID
     * @return Reply
     */
    @DeleteMapping("/v1.0/roles/{id}")
    public Reply deleteRole(@RequestHeader("loginInfo") String info, @PathVariable Long id) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.deleteRole(loginInfo, id);
    }

    /**
     * 添加角色成员
     *
     * @param info    用户关键信息
     * @param id      角色ID
     * @param members 角色成员集合
     * @return Reply
     */
    @PostMapping("/v1.0/roles/{id}/members")
    public Reply addMembers(@RequestHeader("loginInfo") String info, @PathVariable Long id, @RequestBody List<MemberDto> members) {
        if (members == null || members.isEmpty()) {
            return ReplyHelper.invalidParam("请选择需要添加的成员");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        return service.addMembers(loginInfo, id, members);
    }

    /**
     * 移除角色成员
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param member 角色成员DTO
     * @return Reply
     */
    @DeleteMapping("/v1.0/roles/{id}/members")
    public Reply removeMember(@RequestHeader("loginInfo") String info, @PathVariable Long id, @RequestBody MemberDto member) {
        if (member == null) {
            return ReplyHelper.invalidParam("请选择需要移除的成员");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        return service.removeMember(loginInfo, id, member);
    }

    /**
     * 设置角色权限
     *
     * @param info   用户关键信息
     * @param id     角色ID
     * @param permit 角色权限
     * @return Reply
     */
    @PutMapping("/v1.0/roles/{id}/funcs")
    public Reply setFuncPermits(@RequestHeader("loginInfo") String info, @PathVariable Long id, @RequestBody FuncPermitDto permit) {
        if (permit == null) {
            return ReplyHelper.invalidParam("请选择需要授与的功能权限");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        return service.setFuncPermit(loginInfo, id, permit);
    }

    /**
     * 获取日志列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    @GetMapping("/v1.0/roles/logs")
    public Reply getRoleLogs(SearchDto search) {

        return service.getRoleLogs(search);
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/logs/{id}")
    public Reply getRoleLog(@PathVariable Long id) {
        if (id == null) {
            return ReplyHelper.invalidParam();
        }

        return service.getRoleLog(id);
    }
}
