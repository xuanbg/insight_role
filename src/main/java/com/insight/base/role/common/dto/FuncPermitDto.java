package com.insight.base.role.common.dto;

import com.insight.util.Json;

import java.io.Serializable;

/**
 * @author 宣炳刚
 * @date 2019/12/6
 * @remark 角色成员DTO
 */
public class FuncPermitDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 资源ID
     */
    private String id;

    /**
     * 资源父ID
     */
    private String parentId;

    /**
     * 排序字段
     */
    private Integer index;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 授权类型:0.拒绝;1.允许
     */
    private Boolean permit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    @Override
    public String toString() {
        return Json.toJson(this);
    }
}
