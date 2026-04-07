package com.rz.lease.web.admin.service.impl;
import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.web.admin.service.LeaseAgreementService;
import com.rz.lease.web.admin.repository.LeaseAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* @author liubo
* @description 数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class LeaseAgreementServiceImpl implements LeaseAgreementService {
    @Autowired
    private LeaseAgreementRepository leaseAgreementRepository;
}
