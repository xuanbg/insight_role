package com.insight.base.role.common.mapper;

import com.insight.base.role.common.dto.FuncPermitDto;
import com.insight.base.role.common.entity.Role;
import com.insight.utils.common.JsonTypeHandler;
import com.insight.utils.pojo.Log;
import com.insight.utils.pojo.MemberDto;
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
    @Select("select * from ibr_role where app_id = #{appId} and tenant_id is null and is_builtin = 1;")
    List<Role> getTemplates(String appId);

    /**
     * 获取模板角色功能授权集合
     *
     * @param id 角色模板ID
     * @return 角色功能授权集合
     */
    @Select("select function_id as id, permit from ibr_role_permit where role_id = #{id};")
    List<FuncPermitDto> getFuncPermits(String id);

    /**
     * 新增角色
     *
     * @param role 角色DTO
     */
    @Insert("insert ibr_role(id, tenant_id, app_id, name, remark, is_builtin, creator, creator_id, created_time) values " +
            "(#{id}, #{tenantId}, #{appId}, #{name}, #{remark}, #{isBuiltin}, #{creator}, #{creatorId}, #{createdTime});")
    void addRole(Role role);

    /**
     * 添加角色成员
     *
     * @param id      角色ID
     * @param members 角色成员集合
     */
    @Insert("<script>insert ibr_role_member (id, type, role_id, member_id) values " +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">" +
            "(replace(uuid(), '-', ''), #{item.type}, #{id}, #{item.id})</foreach>;</script>")
    void
    addMembers(@Param("id") String id, @Param("list") List<MemberDto> members);

    /**
     * 获取指定角色ID和功能ID的功能授权信息
     *
     * @param id     角色ID
     * @param funcId 功能ID
     * @return 功能授权信息
     */
    @Select("select * from ibr_role_permit where role_id = #{id} and function_id = #{funcId};")
    FuncPermitDto getFuncPermit(@Param("id") String id, @Param("funcId") String funcId);

    /**
     * 添加角色功能授权
     *
     * @param id     角色ID
     * @param funcId 功能ID
     * @param permit 授权状态
     */
    @Insert("insert ibr_role_permit (id, role_id, function_id, permit) values (replace(uuid(), '-', ''), #{id}, #{funcId}, #{permit});")
    void addFuncPermit(@Param("id") String id, @Param("funcId") String funcId, @Param("permit") Boolean permit);

    /**
     * 更新角色功能授权
     *
     * @param id     角色ID
     * @param funcId 功能ID
     * @param permit 授权状态
     */
    @Update("update ibr_role_permit set permit = #{permit} where role_id = #{id} and function_id = #{funcId};")
    void setFuncPermit(@Param("id") String id, @Param("funcId") String funcId, @Param("permit") Boolean permit);

    /**
     * 移除角色功能授权
     *
     * @param id     角色ID
     * @param funcId 功能ID
     */
    @Delete("delete from ibr_role_permit where role_id = #{id} and function_id = #{funcId};")
    void removeFuncPermit(@Param("id") String id, @Param("funcId") String funcId);

    /**
     * 添加角色功能授权
     *
     * @param id      角色ID
     * @param permits 角色功能授权集合
     */
    @Insert("<script>insert ibr_role_permit (id, role_id, function_id, permit) values " +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">" +
            "(replace(uuid(), '-', ''), #{id}, #{item.id}, #{item.permit})</foreach>;</script>")
    void addFuncPermits(@Param("id") String id, @Param("list") List<FuncPermitDto> permits);

    /**
     * 记录操作日志
     *
     * @param log 日志DTO
     */
    @Insert("insert ibl_operate_log(id, tenant_id, type, business, business_id, content, creator, creator_id, created_time) values " +
            "(#{id}, #{tenantId}, #{type}, #{business}, #{businessId}, #{content, typeHandler = com.insight.utils.common.JsonTypeHandler}, " +
            "#{creator}, #{creatorId}, #{createdTime});")
    void addLog(Log log);

    /**
     * 获取操作日志列表
     *
     * @param tenantId 租户ID
     * @param business 业务类型
     * @param key      查询关键词
     * @return 操作日志列表
     */
    @Select("<script>select id, type, business, business_id, creator, creator_id, created_time from ibl_operate_log where business = #{business} " +
            "<if test = 'tenantId != null'>and tenant_id = #{tenantId} </if>" +
            "<if test = 'tenantId == null'>and tenant_id is null </if>" +
            "<if test = 'key!=null'>and (type = #{key} or business = #{key} or business_id = #{key} or creator = #{key} or creator_id = #{key}) </if>" +
            "order by created_time</script>")
    List<Log> getLogs(@Param("tenantId") String tenantId, @Param("business") String business, @Param("key") String key);

    /**
     * 获取操作日志列表
     *
     * @param id 日志ID
     * @return 操作日志列表
     */
    @Results({@Result(property = "content", column = "content", javaType = Object.class, typeHandler = JsonTypeHandler.class)})
    @Select("select * from ibl_operate_log where id = #{id};")
    Log getLog(String id);
}
