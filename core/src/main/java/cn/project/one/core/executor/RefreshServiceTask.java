package cn.project.one.core.executor;

import cn.hutool.cron.CronUtil;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.registrar.AbstractServiceRegistry;

import javax.annotation.PostConstruct;

/**
 * 刷新节点任务任务
 */
public class RefreshServiceTask {

    private final ProjectOneProperties properties;

    private final AbstractServiceRegistry nodeRegistry;

    @PostConstruct
    public void autoRefresh() {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        CronUtil.schedule(properties.getCorn(), new RefreshServiceTimer(nodeRegistry));
        CronUtil.schedule(properties.getBeat(), new BeatTask(nodeRegistry));
    }

    public RefreshServiceTask(ProjectOneProperties properties, AbstractServiceRegistry nodeRegistry) {
        this.properties = properties;
        this.nodeRegistry = nodeRegistry;
    }
}
