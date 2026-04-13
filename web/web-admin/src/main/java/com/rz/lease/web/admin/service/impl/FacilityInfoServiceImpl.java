package com.rz.lease.web.admin.service.impl;

import java.util.List;

import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.admin.service.FacilityInfoService;
import com.rz.lease.web.admin.repository.FacilityInfoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class FacilityInfoServiceImpl implements FacilityInfoService {
    @Autowired
    private FacilityInfoRepository facilityInfoRepository;

    @Override
    public List<FacilityInfo> listFacilityInfo(ItemType type) {
        if (type == null) {
            return facilityInfoRepository.findAll();
        }
        return facilityInfoRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("type"), type));
    }

    @Override
    public void saveOrUpdateFacilityInfo(FacilityInfo facilityInfo) {
        if (facilityInfo.getId() == null) {
            facilityInfoRepository.save(facilityInfo);
            return;
        }
        FacilityInfo existing = facilityInfoRepository.findById(facilityInfo.getId())
                .orElseThrow(() -> new RuntimeException("FacilityInfo not found"));

        existing.setName(facilityInfo.getName());
        existing.setType(facilityInfo.getType());
        existing.setIcon(facilityInfo.getIcon());
        facilityInfoRepository.save(existing);
    }

    @Transactional
    @Override
    public boolean deleteFacilityInfoById(Long id) {
        if (!facilityInfoRepository.existsById(id)) {
            return false;
        }
        facilityInfoRepository.deleteById(id);
        return true;
    }
}
