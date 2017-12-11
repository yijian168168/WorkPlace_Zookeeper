package com.zookeeper.curator;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * Created by ZhangQingrong on 2017/2/23.
 */
@Component
public class ZookeeperLeaderLatch {

    @Value("${quartzZookeeperLockPath}")
    private String quartzZookeeperLockPath;
    @Value("${zookeeperUrl}")
    private String zookeeperUrl;
    @Autowired
    private JobLoaderListener jobLoaderListener;

    private CuratorFramework curatorFramework;
    private LeaderLatch leaderLatch;

    /**
     * 定时任务 leader选举
     */
    @PostConstruct
    public void quartzLeaderLatch() throws Exception {
        String id = DateFormatUtils.format(new Date(), "yyyyMMddHhmmssSSS");
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        curatorFramework = CuratorFrameworkFactory.newClient(zookeeperUrl, retry);
        curatorFramework.start();
        leaderLatch = new LeaderLatch(curatorFramework, quartzZookeeperLockPath, id);
        leaderLatch.addListener(jobLoaderListener);
        leaderLatch.start();
    }

    @PreDestroy
    public void destoryQuartz() {
        try {
            System.out.println("开始摧毁定时任务监听");
            curatorFramework.close();
            leaderLatch.close();
            System.out.println("摧毁定时任务监听完毕");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
