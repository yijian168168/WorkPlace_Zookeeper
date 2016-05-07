package com.zookeeper.onenode.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置文件
 *
 * Created by zhangqingrong on 2016/5/4.
 */
@Data
@Component
public class AppProperties {

    @Value("${zookeeper.ip}")
    private String ip;

    @Value("${zookeeper.timeout}")
    private String timeout;
}
