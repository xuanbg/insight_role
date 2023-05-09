package com.insight.base.role.common.mapper;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.entity.Role;
import com.insight.utils.pojo.user.MemberDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019/12/5
 * @remark 角色核心DAL
 */
@Mapper
public interface CoreMapper {

    /**
     * 获取角色模板
     *
     * @param appId 应用ID
     * @return 角色模板
     */
    @Select("select * from ibr_role where app_id = #{appId} and tenant_id is null and builtin = 1;")
    List<Role> getTemplates(Long appId);

    /**
     * 获取模板角色功能授权集合
     *
     * @param id 角色模板ID
     * @return 角色功能授权集合
     */
    @Select("select function_id as id, permit from ibr_role_permit where role_id = #{id};")
    List<FuncPermitDto> getFuncPermits(Long id);

    /**
     * 新增角色
     *
     * @param role 角色DTO
     */
    @Insert("insert ibr_role(id, tenant_id, app_id, name, remark, builtin, creator, creator_id, created_time) values " +
            "(#{id}, #{tenantId}, #{appId}, #{name}, #{remark}, #{builtin}, #{creator}, #{creatorId}, #{createdTime});")
    void addRole(Role role);

    /**
     * 添加角色成员
     *
     * @param id      角色ID
     * @param members 角色成员集合
     */
    @Insert("<script>insert ibr_role_member (type, role_id, member_id) values " +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">" +
            "(#{item.type}, #{id}, #{item.id})</foreach>;</script>")
    void
    addMembers(@Param("id") Long id, @Param("list") List<MemberDto> members);

    /**
     * 获取指定角色ID和功能ID的功能授权信息
     *
     * @param id     角色ID
     * @param funcId 功能ID
     * @return 功能授权信息
     */
    @Select("select * from ibr_role_permit where role_id = #{id} and function_id = #{funcId};")
    FuncPermitDto getFuncPermit(@Param("id") Long id, @Param("funcId") Long funcId);

    /**
     * 添加角色功能授权
     *
     * @param id     角色ID
     * @param funcId 功能ID
     * @param permit 授权状态
     */
    @Insert("insert ibr_role_permit (role_id, function_id, permit) values (#{id}, #{funcId}, #{permit});")
    void addFuncPermit(@Param("id") Long id, @Param("funcId") Long funcId, @Param("permit") Boolean permit);

    /**
     * 更新角色功能授权
     *
     * @param id     角色ID
     * @param funcId 功能ID
     * @param permit 授权状态
     */
    @Update("update ibr_role_permit set permit = #{permit} where role_id = #{id} and function_id = #{funcId};")
    void setFuncPermit(@Param("id") Long id, @Param("funcId") Long funcId, @Param("permit") Boolean permit);

    /**
     * 移除角色功能授权
     *
     * @param id     角色ID
     * @param funcId 功能ID
     */
    @Delete("delete from ibr_role_permit where role_id = #{id} and function_id = #{funcId};")
    void removeFuncPermit(@Param("id") Long id, @Param("funcId") Long funcId);

    /**
     * 添加角色功能授权
     *
     * @param id      角色ID
     * @param permits 角色功能授权集合
     */
    @Insert("<script>insert ibr_role_permit (role_id, function_id, permit) values " +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">" +
            "(#{id}, #{item.id}, #{item.permit})</foreach>;</script>")
    void addFuncPermits(@Param("id") Long id, @Param("list") List<FuncPermitDto> permits);

    /**
     * 更新角色
     *
     * @param role 角色DTO
     */
    @Update("update ibr_role set app_id = #{appId}, name = #{name}, remark = #{remark} where id = #{id};")
    void updateRole(Role role);

    /**
     * 添加角色数据授权
     *
     * @param id      角色ID
     * @param permits 角色数据授权集合
     */
    @Insert("<script>insert ibr_role_permit (role_id, function_id, permit) values " +
            "<foreach collection = \"permits\" item = \"item\" index = \"index\" separator = \",\">" +
            "(#{id}, #{item}, 1)</foreach>;</script>")
    void addDataPermits(Long id, List<Long> permits);

    /**
     * 移除角色数据授权
     *
     * @param id 角色ID
     */
    @Delete("delete from ibr_role_permit where role_id = #{id};")
    void removeDataPermits(Long id);
}
