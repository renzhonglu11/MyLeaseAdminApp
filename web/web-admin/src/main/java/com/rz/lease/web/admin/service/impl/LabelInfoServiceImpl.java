package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.enums.ItemType;
import com.rz.lease.web.admin.service.LabelInfoService;
import com.rz.lease.web.admin.repository.LabelInfoRepository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LabelInfoServiceImpl implements LabelInfoService {
    @Autowired
    private LabelInfoRepository labelInfoRepository;

    @Override
    public List<LabelInfo> listLabelInfo(ItemType type) {
        if (type == null) {
            return labelInfoRepository.findAll();
        }
        // CriteriaBuilder
        return labelInfoRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("type"), type));
    }

    @Override
    public void saveOrUpdateLabelInfo(LabelInfo labelInfo) {
        if (labelInfo.getId() == null) {
            labelInfoRepository.save(labelInfo);
            return;
        }
        LabelInfo existing = labelInfoRepository.findById(labelInfo.getId())
                .orElseThrow(() -> new RuntimeException("LabelInfo not found"));

        existing.setName(labelInfo.getName());
        existing.setType(labelInfo.getType());
    }

    @Transactional
    @Override
    public boolean deleteLabelInfoById(Long id) {
        if (!labelInfoRepository.existsById(id)) {
            return false;
        }
        labelInfoRepository.deleteById(id);
        return true;
    }
}
