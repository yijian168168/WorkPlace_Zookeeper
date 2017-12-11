package com.zookeeper.onenode.curator;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by ZhangQingrong on 2017/2/23.
 */
@Component
public class ZookeeperLockTest {

    @Autowired
    private ZookeeperLock zookeeperLock;

    @PostConstruct
    public void getLock() {
        InterProcessMutex interProcessMutex = zookeeperLock.createInterProcessMutex("/rootPath/billPayChildPathFas_6");
        zookeeperLock.acquire(interProcessMutex);

    }
}
