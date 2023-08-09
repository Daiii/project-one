package cn.project.one.core.loadbalance;

import cn.project.one.common.instance.Instance;

import java.util.List;

public interface LoadBalance {

    List<Instance> getAll();
}
