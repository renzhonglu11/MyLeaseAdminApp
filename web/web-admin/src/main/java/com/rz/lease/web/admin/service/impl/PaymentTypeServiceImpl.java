package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.PaymentType;
import com.rz.lease.web.admin.repository.PaymentTypeRepository;
import com.rz.lease.web.admin.service.PaymentTypeService;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author liubo
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentTypeServiceImpl(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    @Override
    public List<PaymentType> listPaymentType() {
        return paymentTypeRepository.findAll();
    }

    @Transactional
    @Override
    public void saveOrUpdatePaymentType(PaymentType paymentType) {
        if (paymentType.getId() == null) {
            paymentTypeRepository.save(paymentType);
            return;
        }
        PaymentType existing = paymentTypeRepository.findById(paymentType.getId())
                .orElseThrow(() -> new RuntimeException("PaymentType not found"));

        existing.setName(paymentType.getName());
        existing.setPayMonthCount(paymentType.getPayMonthCount());
        existing.setAdditionalInfo(paymentType.getAdditionalInfo());
    }

    @Override
    public boolean deletePaymentTypeById(Long id) {
        if (!paymentTypeRepository.existsById(id)) {
            return false;
        }
        paymentTypeRepository.deleteById(id);
        return true;
    }

}
