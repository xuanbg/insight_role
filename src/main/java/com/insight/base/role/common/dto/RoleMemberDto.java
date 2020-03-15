package com.insight.base.role.common.dto;

import com.insight.util.Json;

import java.io.Serializable;

/**
 * @author 宣炳刚
 * @date 2020/3/15
 * @remark 角色成员DTO
 */
public class RoleMemberDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 成员唯一ID
     */
    private String id;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 级别
     */
    private Integer type;

    /**
     * 排序字段
     */
    private Integer index;

    /**
     * 成员名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return Json.toJson(this);
    }
}
