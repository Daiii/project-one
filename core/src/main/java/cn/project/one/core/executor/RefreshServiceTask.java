package cn.project.one.core.executor;

import javax.annotation.PostConstruct;

import cn.hutool.cron.CronUtil;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.registrar.AbstractServiceRegistry;

/**
 * 刷新节点任务任务
 */
public class RefreshServiceTask {

    private final ProjectOneProperties properties;

    private final AbstractServiceRegistry serviceRegistry;

    @PostConstruct
    public void autoRefresh() {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        CronUtil.schedule(properties.getCorn(), new RefreshServiceTimer(serviceRegistry));
        CronUtil.schedule(properties.getBeat(), new BeatTask(serviceRegistry));
    }

    public RefreshServiceTask(ProjectOneProperties properties, AbstractServiceRegistry serviceRegistry) {
        this.properties = properties;
        this.serviceRegistry = serviceRegistry;
    }
}
