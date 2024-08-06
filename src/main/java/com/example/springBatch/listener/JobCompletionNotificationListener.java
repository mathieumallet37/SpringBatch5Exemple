package com.example.springBatch.listener;

import com.example.springBatch.utils.LoggerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobCompletionNotificationListener extends LoggerConstants implements JobExecutionListener {
    

    public JobCompletionNotificationListener() {
    }


    public void beforeJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.STARTING) {
            log.info(LoggerConstants.JOB_STARTED);
        }
    }
    
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info(LoggerConstants.JOB_COMMPLETED);
        }
    }
}
