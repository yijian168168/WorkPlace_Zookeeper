package com.zookeeper.onenode.junit;

import org.apache.zookeeper.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ZhangQingrong on 2017/4/5.
 */
public class ZooKeeperUtil {

//    private static final String ip = "192.168.1.116:12181";
    private static final String ip = "172.26.15.11:2181";
    private static final int timeout = 10000;

    @Test
    public void test1() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(ip,
                timeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
                if (event.getState() == Event.KeeperState.SyncConnected) {
                }
            }
        });

        String existNode = "/rootPath/accountPayChildPath_test";
        if (null == zk.exists(existNode, true)) {
            //如果根节点不存在则创建根节点
            System.out.println("节点： " + existNode + "不存在 .");
        }else {
            System.out.println("节点： " + existNode + "已存在 .");
        }

        // 创建一个子目录节点
//        zk.create("/rootPath/test2", "testData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        System.out.println(new String(zk.getData("/rootPath", false, null)));
/*
        // 取出子目录节点列表
        System.out.println(zk.getChildren("/root", true));

        // 创建另外一个子目录节点
        zk.create("/root/childPath2", "childData2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(zk.getChildren("/root", true));

        // 修改子目录节点数据
        zk.setData("/root/childPath1", "childData1New".getBytes(), -1);
        byte[] datas = zk.getData("/root/childPath1", true, null);
        String str = new String(datas, "utf-8");
        System.out.println(str);*/

        //删除整个子目录   -1代表version版本号，-1是删除所有版本
        String deletePath = "/rootPath/accountPayChildPath_test";
//        System.out.println(zk.getChildren("/rootPath", true));
//        String deletePath = "/rootPath/accountPayChildPath_test";
        zk.delete(deletePath, -1);
        System.out.println(zk.getChildren("/rootPath", true));

    }


}
