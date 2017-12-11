package com.zookeeper.onenode.curator;

import com.google.common.base.Objects;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * ZK 锁
 */
@Component
public class ZookeeperLock {

    /**
     * ZK服务器地址
     */
    @Value("${zookeeper.lock.address}")
    private String zkServerUrl;

    /**
     * ZK锁时间
     */
    @Value("${zookeeper.lock.lockTime}")
    private long lockTime;

    private CuratorFramework client = null;

    @PostConstruct
    public void init() {
        connect();
    }

    /**
     * 初始化连接
     */
    private void connect() {
        client = CuratorFrameworkFactory.newClient(zkServerUrl, new ExponentialBackoffRetry(1000, 3));
        client.start();
    }

    /**
     * @param path 锁定路径
     * @return InterProcessMutex
     */
    public InterProcessMutex createInterProcessMutex(String path) {
        return new InterProcessMutex(client, path);
    }

    /**
     * 抢zk锁，重载方法，默认使用配置文件的锁定时间
     *
     * @param lock InterProcessMutex
     */
    public void acquire(InterProcessMutex lock) {
        acquire(lock, lockTime);
    }

    /**
     * 抢zk锁，重载方法，可以自定义锁定时间
     *
     * @param lock     单位(秒)
     * @param lockTime 锁定时间
     */
    public void acquire(InterProcessMutex lock, long lockTime) {
        try {
            if (!lock.acquire(lockTime, TimeUnit.SECONDS)) {
                System.err.println("zk 抢锁失败");
            } else {
                System.out.println("zk抢锁成功");
            }
        } catch (Exception e) {
            System.err.println("zk 抢锁失败" + e.getMessage());
        }
    }

    /**
     * 释放zk锁
     *
     * @param lock  InterProcessMutex
     * @param patch 锁定路径
     */
    public synchronized void release(InterProcessMutex lock, String patch) {
        try {
            if (lock != null) {
                lock.release();
                // 尝试删除锁节点
                client.delete().forPath(patch);
            }
        } catch (KeeperException e) {
            //忽略zk锁子节点存在时删除节点失败
            if (!Objects.equal(e.code().name(), KeeperException.Code.NOTEMPTY.name())) {
                System.err.println("zookeeper lock KeeperException：{}" + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("zookeeper lock release fail：{}" + e.getMessage());
        }
    }
}
