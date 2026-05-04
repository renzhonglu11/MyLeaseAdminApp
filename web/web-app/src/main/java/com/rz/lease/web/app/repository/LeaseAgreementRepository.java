package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.LeaseAgreement;

import java.util.List;

public interface LeaseAgreementRepository extends BaseJpaRepository<LeaseAgreement> {
    List<LeaseAgreement> findByPhoneOrderByIdDesc(String phone);
}
