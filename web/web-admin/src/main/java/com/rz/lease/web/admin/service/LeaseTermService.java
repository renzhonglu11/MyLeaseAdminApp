package com.rz.lease.web.admin.service;

import java.util.List;

import com.rz.lease.model.entity.LeaseTerm;

/**
 * @author rz
 * @description 针对表【lease_term(租期)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface LeaseTermService {

    List<LeaseTerm> listLeaseTerm();

    void saveOrUpdateLeaseTerm(LeaseTerm leaseTerm);

    boolean deleteLeaseTermById(Long id);
}
