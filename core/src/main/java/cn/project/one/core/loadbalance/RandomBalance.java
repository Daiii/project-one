package cn.project.one.core.loadbalance;

import java.util.List;
import java.util.Random;

import cn.project.one.common.instance.Instance;

/**
 * 随机负载均衡
 * 
 * @since 2023/7/28
 */
public class RandomBalance extends AbstractBalance {

    @Override
    public Instance get(List<Instance> groupService) {
        Random random = new Random();
        int index = random.nextInt(groupService.size());
        return groupService.get(index);
    }

    private RandomBalance() {

    }

    private static class Holder {
        static final RandomBalance INSTANCE = new RandomBalance();
    }

    public static RandomBalance getInstance() {
        return Holder.INSTANCE;
    }
}
