package cn.project.one.core.executor;

import javax.annotation.PostConstruct;

import cn.hutool.cron.CronUtil;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.registrar.AbstractRegistry;

/**
 * 刷新节点任务任务
 */
public class RefreshServiceTask {

    private final ProjectOneProperties properties;

    private final AbstractRegistry nodeRegistry;

    @PostConstruct
    public void autoRefresh() {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        CronUtil.schedule(properties.getCorn(), new RefreshServiceTimer(nodeRegistry));
    }

    public RefreshServiceTask(ProjectOneProperties properties, AbstractRegistry nodeRegistry) {
        this.properties = properties;
        this.nodeRegistry = nodeRegistry;
    }
}
