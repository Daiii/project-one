package cn.project.one.core.loadbalance;

import cn.project.one.common.instance.Instance;

import java.util.List;

public interface LoadBalance {

    /**
     * 获取所有服务列表
     * 
     * @return List
     */
    List<Instance> getAll();
}
