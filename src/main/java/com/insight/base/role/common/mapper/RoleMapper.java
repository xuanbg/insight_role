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
     * @param search      查询关键词
     * @return 角色列表
     */
    @Select("""
            <script>select r.id, r.app_id, a.name as app_name, r.name, r.remark, r.builtin, r.creator, r.created_time
            from ibr_role r join ibs_application a on a.id = r.app_id where
            <if test = 'tenantId != null'>r.tenant_id = #{tenantId}</if>
            <if test = 'tenantId == null'>r.tenant_id is null</if>
            <if test = 'appId != null'>and r.app_id = #{appId}</if>
            <if test = 'keyword != null'>and (r.name like concat('%',#{keyword},'%') or a.name like concat('%',#{keyword},'%'))</if>
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
     * @param id 角色ID
     * @return 功能权限列表
     */
    @Select("with apps as (select a.id, null as parent_id, 0 as type, a.index, a.name, null as permit, a.index as i1, null as i2, " +
            "null as i3, null as i4, null as i5 from ibr_role r join ibs_application a on a.id = r.app_id where r.id = #{id}), " +
            "navs as (select n.id, n.app_id as parent_id, n.type, n.index, n.name, null as permit, a.i1, n.index as i2, " +
            "null as i3, null as i4, null as i5 from apps a join ibs_navigator n on n.app_id = a.id and n.parent_id is null), " +
            "mods as (select m.id, m.parent_id, m.type, m.index, m.name, null as permit, n.i1, n.i2, " +
            "m.index as i3, null as i4, null as i5 from navs n join ibs_navigator m on m.parent_id = n.id), " +
            "funs as (select f.id, f.nav_id as parent_id, ifnull(p.permit, 2) + 3 as type, f.index, f.name, p.permit, m.i1, m.i2, " +
            "m.i3, f.type as i4, f.index as i5 from mods m join ibs_function f on f.nav_id = m.id " +
            "left join ibr_role_permit p on p.function_id = f.id and p.role_id = #{id}), " +
            "tree as (select * from apps union select * from navs union select * from mods union select * from funs) " +
            "select * from tree order by i1, i2, i3, i4, i5;")
    List<FuncPermitDto> getFuncPermits(Long id);

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
     * 更新角色
     *
     * @param role 角色DTO
     */
    @Update("update ibr_role set app_id = #{appId}, name = #{name}, remark = #{remark} where id = #{id};")
    void updateRole(Role role);

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
