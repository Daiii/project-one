package cn.project.one.core.executor;

import javax.annotation.PostConstruct;

import cn.hutool.cron.CronUtil;
import cn.project.one.common.config.ProjectOneProperties;
import cn.project.one.core.registrar.AbstractRegistrar;

/**
 * 刷新节点任务任务
 */
public class RefreshServiceTask {

    private final ProjectOneProperties properties;

    private AbstractRegistrar nodeRegistrar;

    @PostConstruct
    public void autoRefresh() {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        CronUtil.schedule(properties.getCorn(), new RefreshServiceTimer(properties, nodeRegistrar));
    }

    public RefreshServiceTask(ProjectOneProperties properties, AbstractRegistrar nodeRegistrar) {
        this.properties = properties;
        this.nodeRegistrar = nodeRegistrar;
    }
}
