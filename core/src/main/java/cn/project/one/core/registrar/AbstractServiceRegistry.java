package cn.project.one.core.registrar;

import java.util.HashMap;

import cn.project.one.common.Node;
import cn.project.one.common.instance.Instance;

public abstract class AbstractServiceRegistry implements ServiceRegistry {

    /**
     * 注册服务
     * 
     * @param node 节点信息
     */
    public abstract void register(Node node);

    /**
     * 注销服务
     * 
     * @param id 节点信息
     */
    public abstract void deregister(String id);

    /**
     * 拉取所有服务
     * 
     * @return HashMap
     */
    public abstract HashMap<String, Instance> services();

    /**
     * 心跳
     * 
     * @param node 节点信息
     */
    public abstract void beat(Node node);
}
