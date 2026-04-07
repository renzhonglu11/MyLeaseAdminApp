package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.FeeKey;
import com.rz.lease.web.admin.vo.fee.FeeKeyVo;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【fee_key(杂项费用名称表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface FeeKeyService {

    void saveOrUpdateFeeKey(FeeKey feeKey);

    List<FeeKeyVo> listFeeInfo();

    boolean deleteFeeKeyById(Long feeKeyId);
}
