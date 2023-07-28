package cn.project.one.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetUtil {

    public static String getHost() {
        String hostName = "127.0.0.1";
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostName();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }
}
