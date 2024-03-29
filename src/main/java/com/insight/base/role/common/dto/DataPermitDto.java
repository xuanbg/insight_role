package com.insight.base.role.common.dto;

import com.insight.utils.pojo.base.BaseXo;

/**
 * @author 宣炳刚
 * @date 2019/12/6
 * @remark 角色成员DTO
 */
public class DataPermitDto extends BaseXo {

    /**
     * 数据所有者ID,相对模式下为模式ID
     */
    private Long id;

    /**
     * 业务模块ID
     */
    private Long moduleId;

    /**
     * 业务模块名称
     */
    private String module;

    /**
     * 排序字段
     */
    private Integer index;

    /**
     * 授权模式:0.相对模式;1.用户模式;2.部门模式
     */
    private Integer mode;

    /**
     * 数据所有者名称
     */
    private String name;

    /**
     * 授权类型:0.只读;1.读写
     */
    private Boolean permit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPermit() {
        return permit;
    }

    public void setPermit(Boolean permit) {
        this.permit = permit;
    }
}
