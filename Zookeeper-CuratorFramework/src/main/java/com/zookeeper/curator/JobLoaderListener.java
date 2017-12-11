package com.zookeeper.curator;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.springframework.stereotype.Component;


/**
 * 定时任务加载器
 * Created by ZhangQingrong on 2017/2/23.
 */
@Slf4j
@Component
public class JobLoaderListener implements LeaderLatchListener {


    @Override
    public void isLeader() {
        System.out.println("this ip is leader now , start to load quartz ");
        try {
//            Thread.sleep(60000);
        } catch (Exception e) {
            System.out.println("start schedulerJob throws unknow exception :{}"+ Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void notLeader() {
        System.out.println("this ip is not leader now , the quartz will shutdown .");
        try {
        } catch (Exception e) {
            System.out.println("shutdown schedulerJob throws unknow exception :{}" +Throwables.getStackTraceAsString(e));
        }
    }

}
