package com.rz.lease.web.admin.schedule;

import com.rz.lease.web.admin.service.LeaseAgreementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.rz.lease.web.WebAdminApplication;

@SpringBootTest(classes = WebAdminApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScheduleTaskTest {

    @Autowired
    private LeaseAgreementService leaseAgreementService;

    @Test
    void checkLeaseStatus_shouldCallService_whenSchedulerRuns() {
        leaseAgreementService.checkLeaseStatus();
    }
}