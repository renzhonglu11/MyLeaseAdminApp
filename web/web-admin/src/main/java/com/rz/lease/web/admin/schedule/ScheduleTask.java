package com.rz.lease.web.admin.schedule;

import java.util.List;

import com.rz.lease.web.admin.service.LeaseAgreementService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    // @Scheduled(cron = "* * * * * *")
    // public void test() {
    // System.out.println(new Date());
    // }

    private LeaseAgreementService leaseAgreementService;

    public ScheduleTask(LeaseAgreementService leaseAgreementService) {
        this.leaseAgreementService = leaseAgreementService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkLeaseStatus() {
        List<Long> expiredAgreementIds = leaseAgreementService.checkLeaseStatus();
        if (!expiredAgreementIds.isEmpty()) {
            System.out.println(expiredAgreementIds);
        }
    }

}
