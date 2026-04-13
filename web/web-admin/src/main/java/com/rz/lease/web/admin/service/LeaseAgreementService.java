package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.rz.lease.web.admin.vo.agreement.AgreementVo;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * @author rz
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface LeaseAgreementService {

    void saveOrUpdate(LeaseAgreement leaseAgreement);

    Page<AgreementVo> page(long current, long size, AgreementQueryVo queryVo);

    AgreementVo getAgreementById(Long id);

    boolean removeById(Long id);

    boolean updateStatusById(Long id, LeaseStatus status);

    List<Long> checkLeaseStatus();

}
