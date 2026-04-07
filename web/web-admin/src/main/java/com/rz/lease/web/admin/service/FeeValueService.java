package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.FeeValue;

/**
 * @author liubo
 * @description 针对表【fee_value(杂项费用值表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface FeeValueService {

    void saveOrUpdateFeeValue(FeeValue feeValue);

    boolean deleteFeeValueById(Long id);
}
