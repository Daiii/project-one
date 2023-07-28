package cn.project.one.core.executor;

import javax.annotation.PostConstruct;

import cn.hutool.cron.CronUtil;
import cn.project.one.common.config.ProjectOneProperties;

public class ServiceRefreshExecutor {

    private final ProjectOneProperties properties;

    @PostConstruct
    public void autoRefresh() {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        CronUtil.schedule(properties.getCorn(), new RefreshTimer(properties));
    }

    public ServiceRefreshExecutor(ProjectOneProperties properties) {
        this.properties = properties;
    }
}
