package com.zookeeper.onenode.junit;

import org.apache.zookeeper.*;

/**
 * Zookeeper 基础功能测试
 * Created by zhangqingrong on 2016/5/3.
 */
public class ZookeeperTest {

    public static void main(String[] args) {

        String ip = "192.168.223.129:6003";
        int timeout = 40000;
        try {
            ZooKeeper zk = new ZooKeeper(ip,
                    timeout, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });

            // 创建一个目录节点
            /**
             * CreateMode:
             *  PERSISTENT (持续的，相对于EPHEMERAL，不会随着client的断开而消失)
             *  PERSISTENT_SEQUENTIAL（持久的且带顺序的）
             *  EPHEMERAL (短暂的，生命周期依赖于client session)
             *  EPHEMERAL_SEQUENTIAL  (短暂的，带顺序的)
             */
            if (null == zk.exists("/root", true)) {
                //如果根节点不存在则创建根节点
                zk.create("/root", "rootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            zk.setData("/root","rootDataNew".getBytes(),-1);

            // 创建一个子目录节点
            zk.create("/root/childPath1", "childData1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(new String(zk.getData("/root", false, null)));

            // 取出子目录节点列表
            System.out.println(zk.getChildren("/root", true));

            // 创建另外一个子目录节点
            zk.create("/root/childPath2", "childData2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(zk.getChildren("/root", true));

            // 修改子目录节点数据
            zk.setData("/root/childPath1", "childData1New".getBytes(), -1);
            byte[] datas = zk.getData("/root/childPath1", true, null);
            String str = new String(datas, "utf-8");
            System.out.println(str);

            //删除整个子目录   -1代表version版本号，-1是删除所有版本
            zk.delete("/root/childPath1", -1);
            System.out.println(zk.getChildren("/root", true));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
