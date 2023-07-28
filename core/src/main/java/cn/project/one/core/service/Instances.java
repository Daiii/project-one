package cn.project.one.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.map.MapUtil;
import cn.project.one.common.instance.Instance;

public class Instances {

    public static Map<String, Instance> INSTANCES = new HashMap<>();

    public static Map<String, List<Instance>> GROUP = new HashMap<>();

    private Instances() {

    }

    public static Instance get(String name) {
        if (MapUtil.isEmpty(GROUP)) {
            return null;
        }

        for (String s : INSTANCES.keySet()) {
            if (INSTANCES.get(s).getService().equals(name)) {
                return INSTANCES.get(s);
            }
        }

        return null;
    }

    public static List<Instance> getGroup(String name) {
        return GROUP.get(name);
    }
}
