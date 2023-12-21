package cn.project.one.common.util;

import cn.hutool.system.HostInfo;

public class InetUtil {

    private static final HostInfo HOST_INFO = new HostInfo();

    /**
     * 获取本机IP
     *
     * @return 主机地址
     */
    public static String getHost() {
        return HOST_INFO.getAddress();
    }

    /**
     * 取得当前主机的名称。
     * 
     * @return 主机名
     */
    public static String getName() {
        return HOST_INFO.getName();
    }
}
