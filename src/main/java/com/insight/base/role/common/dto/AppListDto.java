package com.insight.base.role.common.dto;

import com.insight.util.Json;

import java.io.Serializable;

/**
 * @author 宣炳刚
 * @date 2020/3/14
 * @remark 角色可选应用DTO
 */
public class AppListDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 应用ID
     */
    private String id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用简称
     */
    private String alias;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return Json.toJson(this);
    }
}
