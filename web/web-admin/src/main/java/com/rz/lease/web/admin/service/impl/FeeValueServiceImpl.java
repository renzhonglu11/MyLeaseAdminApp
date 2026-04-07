package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ApartmentFeeValue;
import com.rz.lease.model.entity.FeeKey;
import com.rz.lease.model.entity.FeeValue;
import com.rz.lease.web.admin.repository.ApartmentFeeValueRepository;
import com.rz.lease.web.admin.service.FeeValueService;
import com.rz.lease.web.admin.repository.FeeValueRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class FeeValueServiceImpl implements FeeValueService {
    private final FeeValueRepository feeValueRepository;
    private final ApartmentFeeValueRepository apartmentFeeValueRepository;

    public FeeValueServiceImpl(FeeValueRepository feeValueRepository,
            ApartmentFeeValueRepository apartmentFeeValueRepository) {
        this.feeValueRepository = feeValueRepository;
        this.apartmentFeeValueRepository = apartmentFeeValueRepository;
    }

    private Long resolveFeeKeyId(FeeValue feeValue) {
        if (feeValue.getFeeKeyId() != null) {
            return feeValue.getFeeKeyId();
        }

        FeeKey feeKey = feeValue.getFeeKey();
        if (feeKey != null) {
            return feeKey.getId();
        }

        return null;
    }

    @Override
    public void saveOrUpdateFeeValue(FeeValue feeValue) {
        Long feeKeyId = resolveFeeKeyId(feeValue);
        feeValue.setFeeKeyId(feeKeyId);

        if (feeValue.getId() == null) {
            feeValueRepository.save(feeValue);
            return;
        }
        FeeValue existing = feeValueRepository.findById(feeValue.getId())
                .orElseThrow(() -> new RuntimeException("FeeValue not found"));

        existing.setName(feeValue.getName());
        existing.setUnit(feeValue.getUnit());
        existing.setFeeKeyId(feeKeyId);
        feeValueRepository.save(existing);

    }

    @Transactional
    @Override
    public boolean deleteFeeValueById(Long id) {
        if (!feeValueRepository.existsById(id)) {
            return false;
        }

        List<ApartmentFeeValue> apartmentFeeValues = apartmentFeeValueRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("feeValueId"), id));
        if (!apartmentFeeValues.isEmpty()) {
            apartmentFeeValueRepository.deleteAll(apartmentFeeValues);
        }

        feeValueRepository.deleteById(id);
        return true;
    }
}
