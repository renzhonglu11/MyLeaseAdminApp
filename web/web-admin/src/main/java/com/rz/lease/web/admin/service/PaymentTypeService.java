package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.PaymentType;
import java.util.List;

/**
 * @author rz
 * @description 针对表【payment_type(支付方式表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface PaymentTypeService {

    List<PaymentType> listPaymentType();

    void saveOrUpdatePaymentType(PaymentType paymentType);

    boolean deletePaymentTypeById(Long id);
}
