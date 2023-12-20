package cn.project.one.common.util;

import cn.hutool.system.HostInfo;

public class InetUtil {

    private static final HostInfo HOST_INFO = new HostInfo();

    /**
     * 获取本机IP
     *
     * @return host
     */
    public static String getHost() {
        return HOST_INFO.getAddress();
    }
}
