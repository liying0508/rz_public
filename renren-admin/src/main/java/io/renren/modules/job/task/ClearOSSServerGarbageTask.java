package io.renren.modules.job.task;

import io.renren.common.exception.RenException;
import io.renren.modules.oss.cloud.OSSFactory;
import org.springframework.stereotype.Component;

@Component("clearTask")
public class ClearOSSServerGarbageTask implements ITask {
    public void run(String params) {
        try {
            OSSFactory.build().clearRubbish();
        } catch (Exception e) {
            throw new RenException(10028);
        }
    }
}