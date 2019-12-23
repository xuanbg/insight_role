package com.insight.base.role.common.mapper;

import com.insight.base.role.common.dto.*;
import com.insight.base.role.common.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019/12/4
 * @remark 角色DTO
 */
@Mapper
public interface RoleMapper {

    /**
     * 获取角色列表
     *
     * @param tenantId 租户ID
     * @param key      查询关键词
     * @return 角色列表
     */
    @Select("<script>select r.id, r.app_id, a.name as app_name, r.name, r.remark, r.is_builtin from ibr_role r join ibs_application a on a.id = r.app_id where " +
            "<if test = 'tenantId != null'>r.tenant_id = #{tenantId} </if>" +
            "<if test = 'tenantId == null'>r.tenant_id is null </if>" +
            "<if test = 'key != null'>and (r.name like concat('%',#{key},'%') or a.name like concat('%',#{key},'%')) </if>" +
            "order by r.created_time</script>")
    List<RoleListDto> getRoles(@Param("tenantId") String tenantId, @Param("key") String key);

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    @Select("select r.*, a.name from ibr_role r join ibs_application a on a.id = r.app_id where r.id = #{id};")
    Role getRole(String id);

    /**
     * 更新角色
     *
     * @param role 角色DTO
     */
    @Update("update ibr_role set name = #{name}, remark = #{remark} where id = #{id};")
    void updateRole(Role role);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @Delete("delete r, d, f, m from ibr_role r left join ibr_role_data_permit d on d.role_id = r.id " +
            "left join ibr_role_func_permit f on f.role_id = r.id " +
            "left join ibr_role_member m on m.role_id = r.id where r.id = #{id};")
    void deleteRole(String id);

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return 角色成员集合
     */
    @Select("select u.id, m.type, u.name from ibr_role_member m join ibu_user u on u.id = m.member_id where m.role_id = #{id} union all " +
            "select g.id, m.type, g.name from ibr_role_member m join ibu_group g on g.id = m.member_id where m.role_id = #{id} union all " +
            "select o.id, m.type, o.full_name as name from ibr_role_member m join ibo_organize o on o.id = m.member_id where m.role_id = #{id};")
    List<MemberDto> getMembers(String id);

    /**
     * 查询角色成员用户
     *
     * @param id  角色ID
     * @param key 查询关键词
     * @return 角色成员用户集合
     */
    @Select("<script>select u.id, u.code, u.name, u.account, u.mobile, u.is_invalid from ibv_user_roles m join ibu_user u on u.id = m.user_id " +
            "<if test = 'key != null'>and (u.code = #{key} or u.account = #{key} or u.name like concat('%',#{key},'%')) </if>" +
            "where m.role_id = #{id} order by u.created_time</script>")
    List<MemberUserDto> getMemberUsers(@Param("id") String id, @Param("key") String key);

    /**
     * 移除角色成员
     *
     * @param id     角色ID
     * @param member 角色成员DTO
     */
    @Delete("delete from ibr_role_member where type = #{member.type} and role_id = #{id} and member_id = #{member.id};")
    void removeMember(@Param("id") String id, @Param("member") MemberDto member);

    /**
     * 获取角色功能权限列表
     *
     * @param id 角色ID
     * @return 功能权限列表
     */
    @Select("select t.id, t.parent_id, t.index, t.name, t.permit from (" +
            "select a.id, null as parent_id, a.index, a.name, null as permit, a.index as i1, null as i2, null as i3, null as i4 " +
            "from ibr_role r join ibs_application a on a.id = r.app_id where r.id = #{id} union all " +
            "select n.id, n.app_id as parent_id, n.index, n.name, null as permit, a.index as i1, n.index as i2, null as i3, null as i4 " +
            "from ibr_role r join ibs_application a on a.id = r.app_id join ibs_navigator n on n.app_id = a.id and n.parent_id is null " +
            "where r.id = #{id} union all " +
            "select m.id, m.parent_id, m.index, m.name, null as permit, a.index as i1, n.index as i2, m.index as i3, null as i4 " +
            "from ibr_role r join ibs_application a on a.id = r.app_id join ibs_navigator n on n.app_id = a.id and n.parent_id is null " +
            "join ibs_navigator m on m.parent_id = n.id where r.id = #{id} union all " +
            "select f.id, f.nav_id as parent_id, f.index, f.name, p.permit, a.index as i1, n.index as i2, m.index as i3, f.index as i4 " +
            "from ibr_role r join ibs_application a on a.id = r.app_id join ibs_navigator n on n.app_id = a.id and n.parent_id is null " +
            "join ibs_navigator m on m.parent_id = n.id join ibs_function f on f.nav_id = m.id " +
            "left join ibr_role_func_permit p on p.role_id = r.id and p.function_id = f.id where r.id = #{id}) t order by i1, i2, i3, i4;")
    List<FuncPermitDto> getFuncPermits(String id);

    /**
     * 获取角色数据权限列表
     *
     * @param id 角色ID
     * @return 数据权限列表
     */
    @Select("")
    List<DataPermitDto> getDataPermits(String id);

    /**
     * 添加角色数据授权
     *
     * @param id      角色ID
     * @param permits 角色数据授权集合
     */
    @Insert("<script>insert ibr_role_data_permit (id, role_id, module_id, mode, owner_id, permit) values " +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">" +
            "(replace(uuid(), '-', ''), #{id}, #{item.moduleId}, #{item.mode}, #{item.id}, #{item.permit})</foreach>;</script>")
    void addDataPermits(@Param("id") String id, @Param("list") List<DataPermitDto> permits);

    /**
     * 移除角色数据授权
     *
     * @param id 角色ID
     */
    @Delete("delete from ibr_role_data_permit where role_id = #{id};")
    void removeDataPermits(String id);
}
