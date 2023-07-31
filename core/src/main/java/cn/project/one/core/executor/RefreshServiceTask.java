package cn.project.one.core.executor;

import javax.annotation.PostConstruct;

import cn.hutool.cron.CronUtil;
import cn.project.one.common.config.ProjectOneProperties;

/**
 * 刷新节点任务任务
 */
public class RefreshServiceTask {

    private final ProjectOneProperties properties;

    @PostConstruct
    public void autoRefresh() {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        CronUtil.schedule(properties.getCorn(), new RefreshServiceTimer(properties));
    }

    public RefreshServiceTask(ProjectOneProperties properties) {
        this.properties = properties;
    }
}
