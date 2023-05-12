package com.insight.base.role.common.mapper;

import com.insight.base.role.common.dto.*;
import com.insight.base.role.common.entity.Role;
import com.insight.utils.pojo.base.Search;
import com.insight.utils.pojo.user.MemberDto;
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
     * @param search 查询关键词
     * @return 角色列表
     */
    @Select("""
            <script>select a.name as app_name, r.* from ibr_role r
              join ibs_application a on a.id = r.app_id
            where
            <if test = 'tenantId != null'>r.tenant_id = #{tenantId}</if>
            <if test = 'tenantId == null'>r.tenant_id is null</if>
            <if test = 'appId != null'>and r.app_id = #{appId}</if>
            <if test = 'keyword != null'>and (r.id = #{keyword} or r.name like concat('%',#{keyword},'%')
              or a.name like concat('%',#{keyword},'%'))</if>
            ;</script>
            """)
    List<RoleListDto> getRoles(Search search);

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    @Select("select r.*, a.name from ibr_role r join ibs_application a on a.id = r.app_id where r.id = #{id};")
    Role getRole(Long id);

    /**
     * 更新角色状态
     *
     * @param id     角色ID
     * @param status 可用状态
     */
    @Update("update ibr_role set invalid = #{status} where id = #{id};")
    void updateRoleStatus(Long id, Boolean status);

    /**
     * 获取角色成员
     *
     * @param id 角色ID
     * @return 角色成员集合
     */
    @Select("select u.id, '1' as parent_id, m.type, u.name from ibr_role_member m join ibu_user u on u.id = m.member_id where m.role_id = #{id} union all " +
            "select g.id, '2' as parent_id, m.type, g.name from ibr_role_member m join ibu_group g on g.id = m.member_id where m.role_id = #{id} union all " +
            "select o.id, '3' as parent_id, m.type, o.full_name as name from ibr_role_member m join ibo_organize o on o.id = m.member_id where m.role_id = #{id};")
    List<MemberDto> getMembers(Long id);

    /**
     * 查询角色成员用户
     *
     * @param search 查询关键词
     * @return 角色成员用户集合
     */
    @Select("<script>select u.id, u.code, u.name, u.account, u.mobile, u.invalid from ibv_user_roles m join ibu_user u on u.id = m.user_id " +
            "<if test = 'keyword != null'>and (u.code = #{keyword} or u.account = #{keyword} or u.name like concat('%',#{keyword},'%')) </if>" +
            "where m.role_id = #{id}</script>")
    List<MemberUserDto> getMemberUsers(Search search);

    /**
     * 获取角色功能权限列表
     *
     * @param id     应用ID
     * @param roleId 角色ID
     * @return 功能权限列表
     */
    @Select("""
            select * from (
            select id, parent_id, type, `index`, name, null as permit, i1, null as i2, null as i3, null as i4
            from ibv_auth_navs where parent_id = #{id} union
            select id, parent_id, type, `index`, name, null as permit, i1, i2, null as i3, null as i4
            from ibv_auth_mods where app_id = #{id} union
            select f.id, f.parent_id, f.type, f.`index`, f.name, p.permit, i1, i2, i3, f.`index` as i4
            from ibv_auth_funs f
              left join ibr_role_permit p on p.function_id = f.id and p.role_id = #{roleId}
            where app_id = #{id}) t order by i1, i2, i3, i4;
            """)
    List<FuncPermitDto> getFuncPermits(Long id, Long roleId);

    /**
     * 获取功能权限列表
     *
     * @param id 应用ID
     * @return 功能权限列表
     */
    @Select("""
            select * from (select id, parent_id, type, `index`, name, i1, null as i2, null as i3, null as i4 from ibv_auth_navs where parent_id = #{id}
            union select id, parent_id, type, `index`, name, i1, i2, null as i3, null as i4 from ibv_auth_mods where app_id = #{id}
            union select f.id, f.parent_id, f.type, f.`index`, f.name, i1, i2, i3, f.`index` as i4 from ibv_auth_funs f where app_id = #{id}) t
            order by i1, i2, i3, i4;
            """)
    List<FuncPermitDto> getFuncs(Long id);

    /**
     * 获取角色可选应用列表
     *
     * @param tenantId 租户ID
     * @return 应用列表
     */
    @Select("<script>select a.id, a.`name`, a.alias from ibs_application a " +
            "<if test = 'tenantId != null'>join ibt_tenant_app r on r.app_id = a.id and r.tenant_id = #{tenantId} </if>" +
            "where a.permit_life > 0 order by a.`index`</script>")
    List<AppListDto> getApps(Long tenantId);

    /**
     * 获取角色可选用户成员
     *
     * @param tenantId 租户ID
     * @param roleId   角色ID
     * @return 用户成员集合
     */
    @Select("<script>select u.id, '1' as parent_id, 1 as type, u.`name`, u.remark from ibu_user u " +
            "<if test = 'tenantId != null'>join ibt_tenant_user r on r.user_id = u.id and r.tenant_id = #{tenantId} </if>" +
            "left join ibr_role_member m on m.member_id = u.id and m.type = 1 and m.role_id = #{roleId} " +
            "where u.invalid = 0 and m.id is null order by u.created_time</script>")
    List<RoleMemberDto> getMemberOfUser(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId);

    /**
     * 获取角色可选用户组成员
     *
     * @param tenantId 租户ID
     * @param roleId   角色ID
     * @return 用户组成员集合
     */
    @Select("select g.id, '2' as parent_id, 2 as type, g.`name`, g.remark from ibu_group g " +
            "left join ibr_role_member m on m.member_id = g.id and m.type = 2 and m.role_id = #{roleId} " +
            "where g.tenant_id = #{tenantId} and m.id is null order by g.created_time;")
    List<RoleMemberDto> getMemberOfGroup(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId);

    /**
     * 获取角色可选职位成员
     *
     * @param tenantId 租户ID
     * @param roleId   角色ID
     * @return 职位成员集合
     */
    @Select("select o.id, o.parent_id, o.type, o.`index`, o.`name`, o.remark from ibo_organize o " +
            "left join ibr_role_member m on m.member_id = o.id and m.type = 3 and m.role_id = #{roleId} " +
            "where o.tenant_id = #{tenantId} and m.id is null order by o.`index`;")
    List<RoleMemberDto> getMemberOfTitle(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @Delete("delete r, f, m from ibr_role r left join ibr_role_permit f on f.role_id = r.id left join ibr_role_member m on m.role_id = r.id where r.id = #{id};")
    void deleteRole(Long id);

    /**
     * 移除角色成员
     *
     * @param id     角色ID
     * @param member 角色成员DTO
     */
    @Delete("delete from ibr_role_member where type = #{member.type} and role_id = #{id} and member_id = #{member.id};")
    void removeMember(@Param("id") Long id, @Param("member") MemberDto member);
}
