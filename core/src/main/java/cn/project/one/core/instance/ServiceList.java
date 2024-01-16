package cn.project.one.core.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.map.MapUtil;
import cn.project.one.common.instance.Instance;

/**
 * 本地服务节点
 *
 * @since 2023/7/28
 */
public class ServiceList {

    public static Map<String, Instance> INSTANCES = new HashMap<>();

    public static Map<String, List<Instance>> GROUP = new HashMap<>();

    /**
     * 根据服务ID获取服务
     * 
     * @param id 服务名
     * @return Instance
     */
    public Instance get(String id) {
        if (MapUtil.isEmpty(GROUP)) {
            return null;
        }

        for (String s : INSTANCES.keySet()) {
            if (INSTANCES.get(s).getService().equals(id)) {
                return INSTANCES.get(s);
            }
        }

        return null;
    }

    /**
     * 根据服务名获取所有对应服务
     * 
     * @param name 服务名
     * @return Instance
     */
    public List<Instance> getGroup(String name) {
        return GROUP.get(name);
    }

    /**
     * 获取所有服务
     * 
     * @return List
     */
    public List<Instance> getAll() {
        List<Instance> instances = new ArrayList<>();
        for (String key : INSTANCES.keySet()) {
            instances.add(INSTANCES.get(key));
        }
        return instances;
    }

    private ServiceList() {

    }

    private static class Holder {
        static final ServiceList INSTANCE = new ServiceList();
    }

    public static ServiceList getInstance() {
        return Holder.INSTANCE;
    }
}
