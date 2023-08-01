package cn.project.one.core.loadbalance;

import cn.project.one.common.constants.LoadBalance;

public class LoadBalanceFactory {

    public static AbstractLoadBalance getLoadBalance(LoadBalance loadBalance) {
        if (loadBalance.equals(LoadBalance.Random)) {
            return RandomLoadBalance.getInstance();
        }
        return null;
    }
}
