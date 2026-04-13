package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ApartmentFeeValue;
import com.rz.lease.model.entity.FeeKey;
import com.rz.lease.model.entity.FeeValue;
import com.rz.lease.web.admin.repository.ApartmentFeeValueRepository;
import com.rz.lease.web.admin.repository.FeeValueRepository;
import com.rz.lease.web.admin.service.FeeKeyService;
import com.rz.lease.web.admin.vo.fee.FeeKeyVo;
import com.rz.lease.web.admin.repository.FeeKeyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class FeeKeyServiceImpl implements FeeKeyService {
    private final FeeKeyRepository feeKeyRepository;
    private final FeeValueRepository feeValueRepository;
    private final ApartmentFeeValueRepository apartmentFeeValueRepository;

    public FeeKeyServiceImpl(FeeKeyRepository feeKeyRepository,
            FeeValueRepository feeValueRepository,
            ApartmentFeeValueRepository apartmentFeeValueRepository) {
        this.feeKeyRepository = feeKeyRepository;
        this.feeValueRepository = feeValueRepository;
        this.apartmentFeeValueRepository = apartmentFeeValueRepository;
    }

    @Transactional
    @Override
    public void saveOrUpdateFeeKey(FeeKey feeKey) {
        Long id = feeKey.getId();
        if (id == null) {
            feeKeyRepository.save(feeKey);
            return;
        }

        FeeKey existing = feeKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FeeKey not found"));
        existing.setName(feeKey.getName());
    }

    @Override
    public List<FeeKeyVo> listFeeInfo() {
        List<FeeKey> feeKeys = feeKeyRepository.findAll();
        List<FeeValue> feeValues = feeValueRepository.findAll();

        Map<Long, List<FeeValue>> feeValueMap = feeValues.stream()
                .filter(feeValue -> feeValue.getFeeKeyId() != null)
                .collect(Collectors.groupingBy(FeeValue::getFeeKeyId));

        return feeKeys.stream().map(feeKey -> {
            FeeKeyVo feeKeyVo = new FeeKeyVo();
            feeKeyVo.setId(feeKey.getId());
            feeKeyVo.setName(feeKey.getName());
            feeKeyVo.setFeeValueList(feeValueMap.getOrDefault(feeKey.getId(), Collections.emptyList()));
            return feeKeyVo;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteFeeKeyById(Long feeKeyId) {
        if (!feeKeyRepository.existsById(feeKeyId)) {
            return false;
        }

        List<FeeValue> feeValues = feeValueRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("feeKeyId"), feeKeyId));
        if (!feeValues.isEmpty()) {
            Set<Long> feeValueIds = feeValues.stream()
                    .map(FeeValue::getId)
                    .collect(Collectors.toSet());

            List<ApartmentFeeValue> apartmentFeeValues = apartmentFeeValueRepository.findAll(
                    (root, query, cb) -> root.get("feeValueId").in(feeValueIds));
            if (!apartmentFeeValues.isEmpty()) {
                apartmentFeeValueRepository.deleteAll(apartmentFeeValues);
            }

            feeValueRepository.deleteAll(feeValues);
        }

        feeKeyRepository.deleteById(feeKeyId);
        return true;
    }
}
