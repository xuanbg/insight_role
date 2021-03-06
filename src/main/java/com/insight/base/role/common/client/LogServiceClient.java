package com.insight.base.role.common.client;

import com.insight.base.role.common.config.FeignClientConfig;
import com.insight.utils.pojo.Reply;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 宣炳刚
 * @date 2019-08-31
 * @remark 消息中心Feign客户端
 */
@FeignClient(name = "common-basedata", configuration = FeignClientConfig.class)
public interface LogServiceClient {

    /**
     * 获取日志列表
     *
     * @param business 业务类型
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    @GetMapping("/common/log/v1.0/logs")
    Reply getLogs(@RequestParam String business, @RequestParam(required = false) String keyword, @RequestParam int page, @RequestParam int size);

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @GetMapping("/common/log/v1.0/logs/{id}")
    Reply getLog(@PathVariable Long id);
}
