package cn.project.one.core.loadbalance;

import java.util.List;

import cn.project.one.common.instance.Instance;
import cn.project.one.core.service.ServiceList;

public abstract class AbstractLoadBalance implements LoadBalance {

    abstract Instance get(List<Instance> groupService);

    @Override
    public List<Instance> getAll() {
        return ServiceList.getInstance().getAll();
    }
}
