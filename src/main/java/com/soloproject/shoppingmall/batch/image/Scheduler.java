package com.soloproject.shoppingmall.batch.image;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Scheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job imageJob;

    @Scheduled(cron = "0 */1 * * * *")
    public void imageJobRun() throws
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time",System.currentTimeMillis())
                        .toJobParameters();

        jobLauncher.run(imageJob,jobParameters);
    }
}
