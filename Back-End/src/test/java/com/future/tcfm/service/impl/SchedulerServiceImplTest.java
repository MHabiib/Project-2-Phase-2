package com.future.tcfm.service.impl;

import org.awaitility.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;



import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;
import static org.springframework.test.web.client.ExpectedCount.times;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerServiceImplTest {

    @SpyBean
    private SchedulerServiceImpl myTask;

    @Test
    public void scheduler() {
        await().atMost(Duration.ZERO)
                .untilAsserted(()->verify(myTask, atLeast(1)).scheduler());
    }
    @Test
    public void schedulerReminder() {
        await().atMost(Duration.ZERO)
                .untilAsserted(()->verify(myTask, atLeast(1)).schedulerReminder());
    }
    @Test
    public void monthlyCashStatement() {
        await().atMost(Duration.ZERO)
                .untilAsserted(()->verify(myTask, atLeast(1)).monthlyCashStatement());
    }
}