package cn.project.one.core.loadbalance;

import java.util.List;

import cn.project.one.common.instance.Instance;

public abstract class AbstractLoadBalance {

    abstract Instance get(List<Instance> groupService);
}
