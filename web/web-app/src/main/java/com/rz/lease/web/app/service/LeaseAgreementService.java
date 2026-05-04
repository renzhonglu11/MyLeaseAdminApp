package com.rz.lease.web.app.service;

import com.rz.lease.model.entity.LeaseAgreement;
import com.rz.lease.model.enums.LeaseStatus;
import com.rz.lease.web.app.vo.agreement.AgreementDetailVo;
import com.rz.lease.web.app.vo.agreement.AgreementItemVo;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service
 * @createDate 2023-07-26 11:12:39
 */
public interface LeaseAgreementService {
    List<AgreementItemVo> listItem(String username);

    AgreementDetailVo getDetailById(Long id);

    void updateStatusById(Long id, LeaseStatus leaseStatus);

    void saveOrUpdate(LeaseAgreement leaseAgreement);
}
