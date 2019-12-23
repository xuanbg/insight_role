package com.insight.base.role.manage;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.entity.Role;
import com.insight.util.Json;
import com.insight.util.ReplyHelper;
import com.insight.util.pojo.LoginInfo;
import com.insight.util.pojo.MemberDto;
import com.insight.util.pojo.Reply;
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
     * @param info    用户关键信息
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @GetMapping("/v1.0/roles")
    public Reply getRoles(@RequestHeader("loginInfo") String info, @RequestParam(required = false) String keyword,
                          @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getRoles(loginInfo.getTenantId(), keyword, page, size);
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}")
    public Reply getRole(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ReplyHelper.invalidParam();
        }

        return service.getRole(id);
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
     * @param dto  角色DTO
     * @return Reply
     */
    @PutMapping("/v1.0/roles")
    public Reply editRole(@RequestHeader("loginInfo") String info, @Valid @RequestBody Role dto) {
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
    @DeleteMapping("/v1.0/roles")
    public Reply deleteRole(@RequestHeader("loginInfo") String info, @RequestBody String id) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.deleteRole(loginInfo, id);
    }

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/members")
    public Reply getMembers(@PathVariable String id) {
        return service.getMembers(id);
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
    @GetMapping("/v1.0/roles/{id}/users")
    public Reply getMemberUsers(@PathVariable String id, @RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return service.getMemberUsers(id, keyword, page, size);
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
    public Reply addMembers(@RequestHeader("loginInfo") String info, @PathVariable String id, @RequestBody List<MemberDto> members) {
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
    public Reply removeMember(@RequestHeader("loginInfo") String info, @PathVariable String id, @RequestBody MemberDto member) {
        if (member == null) {
            return ReplyHelper.invalidParam("请选择需要移除的成员");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        return service.removeMember(loginInfo, id, member);
    }

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/{id}/funcs")
    public Reply getFuncPermits(@PathVariable String id) {
        return service.getFuncPermits(id);
    }

    /**
     * 设置角色权限
     *
     * @param info    用户关键信息
     * @param id      角色ID
     * @param permits 角色权限集合
     * @return Reply
     */
    @PutMapping("/v1.0/roles/{id}/funcs")
    public Reply setFuncPermits(@RequestHeader("loginInfo") String info, @PathVariable String id, @RequestBody List<FuncPermitDto> permits) {
        if (permits == null || permits.isEmpty()) {
            return ReplyHelper.invalidParam("请选择需要授与的功能权限");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        return service.setFuncPermits(loginInfo, id, permits);
    }

    /**
     * 获取日志列表
     *
     * @param info    用户关键信息
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @GetMapping("/v1.0/roles/logs")
    public Reply getRoleLogs(@RequestHeader("loginInfo") String info, @RequestParam(required = false) String keyword,
                             @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getRoleLogs(loginInfo.getTenantId(), keyword, page, size);
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @GetMapping("/v1.0/roles/logs/{id}")
    public Reply getRoleLog(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ReplyHelper.invalidParam();
        }

        return service.getRoleLog(id);
    }
}
