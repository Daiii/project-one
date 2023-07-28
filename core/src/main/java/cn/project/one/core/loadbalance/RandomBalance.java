package cn.project.one.core.loadbalance;

import java.util.List;
import java.util.Random;

import cn.project.one.common.instance.Instance;

public class RandomBalance {

    public static Instance get(List<Instance> groupService) {
        Random random = new Random();
        int index = random.nextInt(groupService.size());
        return groupService.get(index);
    }
}
