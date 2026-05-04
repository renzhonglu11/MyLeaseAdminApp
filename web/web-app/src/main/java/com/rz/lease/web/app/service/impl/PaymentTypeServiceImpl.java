package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.PaymentType;
import com.rz.lease.web.app.repository.PaymentTypeRepository;
import com.rz.lease.web.app.repository.RoomPaymentTypeRepository;
import com.rz.lease.web.app.service.PaymentTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;
    private final RoomPaymentTypeRepository roomPaymentTypeRepository;

    public PaymentTypeServiceImpl(PaymentTypeRepository paymentTypeRepository,
                                  RoomPaymentTypeRepository roomPaymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.roomPaymentTypeRepository = roomPaymentTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentType> listByRoomId(Long id) {
        return roomPaymentTypeRepository.findPaymentTypesByRoomId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentType> list() {
        return paymentTypeRepository.findAll();
    }
}
