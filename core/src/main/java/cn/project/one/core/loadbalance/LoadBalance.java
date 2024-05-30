package cn.project.one.core.loadbalance;

import java.util.List;

import cn.project.one.common.instance.Instance;

public interface LoadBalance {

    /**
     * 获取所有服务列表
     * 
     * @return List
     */
    List<Instance> getAll();
}
