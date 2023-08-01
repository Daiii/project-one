package cn.project.one.core.loadbalance;

import java.util.List;
import java.util.Random;

import cn.project.one.common.instance.Instance;

/**
 * 随机负载均衡
 * 
 * @since 2023/7/28
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    public Instance get(List<Instance> groupService) {
        Random random = new Random();
        int index = random.nextInt(groupService.size());
        return groupService.get(index);
    }

    private RandomLoadBalance() {

    }

    private static class Holder {
        static final RandomLoadBalance INSTANCE = new RandomLoadBalance();
    }

    public static RandomLoadBalance getInstance() {
        return Holder.INSTANCE;
    }
}
