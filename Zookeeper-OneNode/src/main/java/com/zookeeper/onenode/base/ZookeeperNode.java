package com.zookeeper.onenode.base;

import com.zookeeper.onenode.constant.AppProperties;
import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zhangqingrong on 2016/5/4.
 */
@Service
public class ZookeeperNode {

    @Autowired
    private AppProperties appProperties;

    ZooKeeper zk = null;

    @PostConstruct
    public void init() throws Exception {
        zk = new ZooKeeper(appProperties.getIp(), Integer.parseInt(appProperties.getTimeout()), new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });

        if (null == zk.exists("/root", true)) {
            //如果根节点不存在则创建根节点
            zk.create("/root", "rootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        rootChildListener();

        rootDataListener();

        System.out.println("启动成功");
    }


    public void rootDataListener() throws Exception {
        zk.exists("/root", new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                Event.EventType eventType = event.getType();
                Event.KeeperState state = event.getState();
                String watchPath = event.getPath();
                try {
                    switch (eventType) {
                        case NodeCreated:
                            System.out.println("节点被创建：");
                            break;
                        case NodeDataChanged:
                            String nodeData = new String(zk.getData("/root", false, null));
                            System.out.println("节点内容被修改：" + nodeData);
                            break;
                        case NodeDeleted:o:
                            System.out.println("节点被删除：");
                            break;
                    }
                    rootChildListener();
                    System.out.println("[" + watchPath + "]已经触发了[" + eventType + "][" + state + "]事件！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void rootChildListener() throws Exception {
        zk.getChildren("/root", new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                Event.EventType eventType = event.getType();
                Event.KeeperState state = event.getState();
                String watchPath = event.getPath();
                try {
                    switch (eventType) {
                        case NodeChildrenChanged:
                            List<String> childNodes = zk.getChildren("/root", false);
                            System.out.println("子节点发生变化：" + childNodes);
                            break;
                        case NodeCreated:
                            System.out.println("节点被创建：");
                            break;
                        case NodeDataChanged:
                            String nodeData = new String(zk.getData("/root", false, null));
                            System.out.println("节点内容被修改：" + nodeData);
                            break;
                    }
                    rootChildListener();
                    System.out.println("[" + watchPath + "]已经触发了[" + eventType + "][" + state + "]事件！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
