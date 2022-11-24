package com.insight.base.role.common.dto;

import com.insight.utils.pojo.base.BaseXo;

/**
 * @author 宣炳刚
 * @date 2019/12/6
 * @remark 角色成员DTO
 */
public class FuncPermitDto extends BaseXo {

    /**
     * 资源ID
     */
    private Long id;

    /**
     * 资源父ID
     */
    private Long parentId;

    /**
     * 级别
     */
    private Integer type;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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

    public Boolean getPermit() {
        return permit;
    }

    public void setPermit(Boolean permit) {
        this.permit = permit;
    }
}
