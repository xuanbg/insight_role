# 角色服务接口说明 v1.0.0

编写说明：

1. 文档版本号请与服务版本号保持一致；
2. 概述和主要功能部分由产品或架构编写；
3. 接口部分(三级目录)按模块(二级目录)进行归类，由开发人员按模板要求的格式编写。

## 目录

- [概述](#概述)
  - [主要功能](#主要功能)
  - [通讯方式](#通讯方式)
- [角色管理](#角色管理)
  - [查询角色列表](#查询角色列表)
  - [获取角色详情](#获取角色详情)
  - [获取角色成员列表](#获取角色成员列表)
  - [获取角色成员用户列表](#获取角色成员用户列表)
  - [获取角色权限列表](#获取角色权限列表)
  - [获取角色可选应用列表](#获取角色可选应用列表)
  - [新增角色](#新增角色)
  - [编辑角色](#编辑角色)
  - [删除角色](#删除角色)
  - [获取日志列表](#获取日志列表)
  - [获取日志详情](#获取日志详情)
- [角色授权](#角色授权)
  - [获取可选用户](#获取可选用户)
  - [获取可选用户组](#获取可选用户组)
  - [添加角色成员](#添加角色成员)
  - [移除角色成员](#移除角色成员)
  - [角色功能授权](#角色功能授权)
- [DTO类型说明](#DTO类型说明)

## 概述

Insight 角色服务是 Insight 基础服务的组成部分之一，提供角色管理及角色授权的相关服务。

### 主要功能

1. 角色管理模块，提供平台/租户的角色管理接口。
2. 角色授权模块，提供对角色进行授权和添加/删除角色成员的相关接口。

### 通讯方式

接口支持 **HTTP/HTTPS** 协议的 **GET/POST/PUT/DELETE** 方法，支持 **URL Params** 、 **Path Variable** 或 **BODY** 传递接口参数。如使用 **BODY** 传参，则需使用 **Json** 格式的请求参数。接口 **/URL** 区分大小写，请求以及返回都使用 **UTF-8** 字符集进行编码，接口返回的数据封装为统一的 **Json** 格式。格式详见：[Reply数据类型](#Reply)。

>注：文档中所列举的类型皆为 **Java** 语言的数据类型，其它编程语言的的数据类型请自行对应。

建议在HTTP请求头中设置以下参数：

|参数名|参数值|
| ----- | ----- |
|Accept|application/json|
|Content-Type|application/json|

## 角色管理

### 查询角色列表

根据关键词(可选)分页查询与租户关联的角色列表。关键词可模糊匹配角色名称。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|String|keyword|否|查询关键词,如查询全部角色,则必须制定查询关键词|
|Integer|page|否|分页页码|
|Integer|size|否|每页记录数|
|Long|appId|否|可根据appId查询与该应用相关的角色，为空时返回全部租户相关角色或平台角色及角色模版|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|角色ID|
|Long|appId|应用ID|
|String|appName|应用名称|
|String|name|角色名称|
|String|remark|备注|
|Boolean|builtin|是否模版|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/roles?appId=168140442586578964" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "id": "196543899395686423",
      "appId": "168140442586578964",
      "appName": "新纪元题库管理系统",
      "name": "题库测试",
      "remark": null,
      "builtin": false
    }
  ],
  "option": "1"
}
```

[回目录](#目录)

### 获取角色详情

获取角色详情信息。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/{id}**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|角色ID|
|Long|tenantId|租户ID|
|Long|appId|应用ID|
|String|appName|应用名称|
|String|name|角色名称|
|String|remark|备注|
|String|creator|创建人|
|Long|creatorId|创建人ID|
|Date|createdTime|创建时间|
|Boolean|builtin|是否模版|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/roles/196543899395686423" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": {
    "id": "196543899395686423",
    "tenantId": null,
    "appId": "168140442586578964",
    "appName": null,
    "name": "题库测试",
    "remark": null,
    "creator": "系统管理员",
    "creatorId": "0",
    "createdTime": "2021-06-26 08:35:17",
    "builtin": false
  },
  "option": null
}
```

[回目录](#目录)

### 获取角色成员列表

获取角色成员列表。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/{id}/members**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|成员ID|
|Long|parentId|成员类型ID：1.用户,2.用户组,3.职位|
|Integer|type|类型：1.用户,2.用户组,3.职位|
|String|code|null|
|String|name|成员名称|
|String|remark|备注|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/roles/196543899395686423/members" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "id": "196543721217458195",
      "parentId": "1",
      "type": 1,
      "code": null,
      "name": "测试",
      "remark": null
    }
  ],
  "option": null
}
```

[回目录](#目录)

### 获取角色成员用户列表

获取角色成员用户列表。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/{id}/users**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|用户ID|
|String|code|用户编码|
|String|name|用户名|
|String|account|登录账号|
|String|mobile|手机号|
|Boolean|invalid|是否禁用|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/roles/196543899395686423/users" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "id": "196543721217458195",
      "code": "IU01018682",
      "name": "测试",
      "account": "test",
      "mobile": null,
      "invalid": false
    }
  ],
  "option": "1"
}
```

[回目录](#目录)

### 获取角色权限列表

获取角色权限列表。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/{id}/funcs**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|导航/功能ID|
|Long|parentId|父级ID：0.一级导航,1.二级导航,2.功能|
|Integer|type|类型：0.一级导航,1.二级导航,2.功能|
|Integer|index|序号|
|String|name|导航/功能名称|
|Boolean|permit|是否授权，仅限功能。null:未授权,true:授权,false:禁止(其他角色授权亦无效)|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/roles/196543899395686423/funcs" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "id": "168140442586578964",
      "parentId": null,
      "type": 0,
      "index": 3,
      "name": "新纪元题库管理系统",
      "permit": null
    },
    {
      "id": "168140442624327700",
      "parentId": "168140442586578964",
      "type": 1,
      "index": 1,
      "name": "题库管理",
      "permit": null
    }
  ],
  "option": null
}
```

[回目录](#目录)

### 获取角色可选应用列表

获取当前租户绑定的应用列表。

请求方法：**POST**

接口URL：**/base/role/v1.0/apps**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|应用ID|
|String|name|应用名称|
|String|alias|应用简称|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/apps" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "id": "134660498556715024",
      "name": "新纪元多租户平台管理系统",
      "alias": "MTP"
    },
    {
      "id": "134661270778413072",
      "name": "新纪元用户及权限管理系统",
      "alias": "RMS"
    },
    {
      "id": "168140442586578964",
      "name": "新纪元题库管理系统",
      "alias": "NPM"
    }
  ],
  "option": null
}
```

[回目录](#目录)

### 新增角色

新增一个角色，并关联到创建人所登录的租户。

请求方法：**POST**

接口URL：**/base/role/v1.0/roles**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|appId|否|应用ID，租户角色必传|
|String|name|是|角色名称|
|String|remark|否|备注|
|Boolean|builtin|是|是否模版|

请求示例：

```bash
curl -X "POST" "http://192.168.0.87:6200/base/role/v1.0/roles" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json' \
     -d $'{
  "appId": "168140442586578964",
  "name": "测试"
}'
```

返回结果示例：

```json
{
  "success": true,
  "code": 201,
  "message": "创建数据成功",
  "data": "299b7d142d624238a7b66300ab3b4f5a",
  "option": null
}
```

[回目录](#目录)

### 编辑角色

更新角色的姓名/昵称/登录账号/手机号/Email/头像URL和备注。属性为空表示该属性更新为NULL。

请求方法：**PUT**

接口URL：**/base/role/v1.0/roles/{id}**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|appId|否|应用ID，租户角色必传|
|String|name|是|角色名称|
|String|remark|否|备注|

请求示例：

```bash
curl -X "PUT" "http://192.168.0.87:6200/base/role/v1.0/roles/198353909591506967" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json' \
     -d $'{
  "appId": "168140442586578964",
  "name": "测试",
  "remark": "!!!"
  "builtin": false,
}'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": null,
  "option": null
}
```

[回目录](#目录)

### 删除角色

删除角色，同时在租户、角色、角色组、组织机构中删除与该角色的关系。

请求方法：**DELETE**

接口URL：**/base/role/v1.0/roles/{id}**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

请求示例：

```bash
curl -X "DELETE" "http://192.168.0.87:6200/base/role/v1.0/roles/198353909591506967" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": null,
  "option": null
}
```

[回目录](#目录)

### 获取日志列表

通过关键词查询接口配置数据变更记录。查询关键词作用于操作类型、业务名称、业务ID、部门ID、操作人ID和操作人姓名。该接口支持分页，如不传分页参数，则返回最近添加的20条数据。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/logs**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|String|keyword|否|查询关键词|
|Integer|page|否|分页页码|
|Integer|size|否|每页记录数|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|日志ID|
|String|type|操作类型|
|String|business|业务名称|
|String|businessId|业务ID|
|String|creator|创建人|
|String|creatorId|创建人ID|
|Date|createdTime|创建时间|

请求示例：

```bash
curl "http://192.168.236.8:6200/base/role/v1.0/roles/logs?keyword=insert" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6IjY3MTg2ZDc4OTNjZTQ0Y2NiODBiN2Q3MGNmYWY1NTFiIiwic2VjcmV0IjoiYzk2MjJiMTM0NTI3NDQ2YWFkODU1MDM3OWFlOGM1MjYifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "id": "b0c781a91ed448ddbb2d8791d80d6ff6",
      "tenantId": null,
      "type": "INSERT",
      "business": "角色管理",
      "businessId": "52fdf8884f6c46609fe67668731236cb",
      "content": null,
      "creator": "系统管理员",
      "creatorId": "bb82f6bdfc5211e99bc30242ac110005",
      "createdTime": "2019-11-04 15:10:41"
    }
  ],
  "option": 12
}
```

[回目录](#目录)

### 获取日志详情

获取指定ID的日志详情。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/logs/{id}**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|日志ID|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Long|id|日志ID|
|String|type|操作类型|
|String|business|业务名称|
|String|businessId|业务ID|
|Object|content|日志内容|
|String|creator|创建人|
|String|creatorId|创建人ID|
|Date|createdTime|创建时间|

请求示例：

```bash
curl "http://192.168.236.8:6200/base/role/v1.0/roles/logs/b0c781a91ed448ddbb2d8791d80d6ff6" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6IjY3MTg2ZDc4OTNjZTQ0Y2NiODBiN2Q3MGNmYWY1NTFiIiwic2VjcmV0IjoiYzk2MjJiMTM0NTI3NDQ2YWFkODU1MDM3OWFlOGM1MjYifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": {
    "id": "b0c781a91ed448ddbb2d8791d80d6ff6",
    "tenantId": null,
    "type": "INSERT",
    "business": "角色管理",
    "businessId": "52fdf8884f6c46609fe67668731236cb",
    "content": {
      "id": "52fdf8884f6c46609fe67668731236cb",
      "code": null,
      "name": "测试",
      "email": null,
      "mobile": null,
      "remark": null,
      "account": "test",
      "builtin": false,
      "creator": "测试",
      "headImg": null,
      "invalid": false,
      "unionId": null,
      "password": "e10adc3949ba59abbe56e057f20f883e",
      "creatorId": "52fdf8884f6c46609fe67668731236cb",
      "createdTime": "2019-11-04 15:10:40",
      "payPassword": null
    },
    "creator": "系统管理员",
    "creatorId": "bb82f6bdfc5211e99bc30242ac110005",
    "createdTime": "2019-11-04 15:10:41"
  },
  "option": null
}
```

[回目录](#目录)

## 角色授权

### 获取可选用户

获取当前角色的可选成员-用户。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/{id}/users/other**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

接口返回数据类型：

|类型|属性|属性说明|
| ----- | ----- | ----- |
|String|id|用户ID|
|Long|parentId|成员类型ID：1.用户,2.用户组,3.职位|
|Integer|type|类型：1.用户,2.用户组,3.职位|
|String|index|序号|
|String|name|用户名|
|String|remark|备注|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/roles/196543899395686423/users/other" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "id": "0",
      "parentId": "1",
      "type": 1,
      "index": null,
      "name": "系统管理员",
      "remark": null
    }
  ],
  "option": null
}
```

[回目录](#目录)

### 获取可选用户组

获取当前角色的可选成员-用户组。

请求方法：**GET**

接口URL：**/base/role/v1.0/roles/{id}/groups/other**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|

请求示例：

```bash
curl "http://192.168.0.87:6200/base/role/v1.0/roles/196543899395686423/groups/other" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": [],
  "option": null
}
```

[回目录](#目录)

### 添加角色成员

添加用户/用户组为所选角色成员，角色成员用户将获得角色授予的权限。

请求方法：**POST**

接口URL：**/base/role/v1.0/roles/{id}/members**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|
|List<MemberDto\>|members|是|角色成员集合|

MemberDto类型：
|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|成员ID|
|Integer|type|是|类型：1.用户,2.用户组,3.职位|

请求示例：

```bash
curl -X "POST" "http://192.168.0.87:6200/base/role/v1.0/roles/198353909591506967/members" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json' \
     -d $'[
  {
    "id": "0",
    "type": 1
  }
]'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": null,
  "option": null
}
```

[回目录](#目录)

### 移除角色成员

移除角色成员，角色成员用户将失去角色授予的权限。

请求方法：**DELETE**

接口URL：**/base/role/v1.0/roles/{id}/members**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|
|List<MemberDto\>|members|是|角色成员集合|

MemberDto类型：
|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|成员ID|
|Integer|type|是|类型：1.用户,2.用户组,3.职位|

请求示例：

```bash
curl -X "DELETE" "http://192.168.0.87:6200/base/role/v1.0/roles/198353909591506967/members" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json' \
     -d $'[
  {
    "id": "0",
    "type": 1
  }
]'
```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": null,
  "option": null
}
```

[回目录](#目录)

### 角色功能授权

角色功能授权。

请求方法：**PUT**

接口URL：**/base/role/v1.0/roles/{id}/funcs**

请求参数如下：

|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|角色ID|
|FuncPermitDto|permit|是|功能权限DTO|

FuncPermitDto类型：
|类型|属性|是否必需|属性说明|
| ----- | ----- | ----- | ----- |
|Long|id|是|成员ID|
|Boolean|permit|是|是否授权：null:未授权,true:授权,false:禁止(其他角色授权亦无效)|

请求示例：

```bash
curl -X "PUT" "http://192.168.0.87:6200/base/role/v1.0/roles/198385963125374999/funcs" \
     -H 'Accept: application/json' \
     -H 'Accept-Encoding: gzip, identity' \
     -H 'Authorization: eyJpZCI6Ijc1NjE2ZTNlMDFhYzQxZGY5ZGRmMjE4MTRjNjk4NzBhIiwic2VjcmV0IjoiMWI2OGU4MTQxN2I4NGExNGJkMTUxOTIwYmJmMGFhNzAifQ==' \
     -H 'Content-Type: application/json' \
     -d $'{
  "id": "134687950754545685",
  "permit": true
}'

```

返回结果示例：

```json
{
  "success": true,
  "code": 200,
  "message": "请求成功",
  "data": null,
  "option": null
}
```

[回目录](#目录)

## DTO类型说明

### Reply

|类型|属性|属性说明|
| ----- | ----- | ----- |
|Boolean|success|接口调用是否成功，成功：true；失败：false|
|Integer|code|错误代码，2xx代表成功，4xx或5xx代表失败|
|String|message|错误消息，描述了接口调用失败原因|
|Object|data|接口返回数据|
|Object|option|附加数据，如分页数据的总条数|

[回目录](#目录)
