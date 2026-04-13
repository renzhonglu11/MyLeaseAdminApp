package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.AttrValue;
import com.rz.lease.model.entity.RoomAttrValue;
import com.rz.lease.web.admin.service.AttrValueService;
import com.rz.lease.web.admin.repository.AttrValueRepository;
import com.rz.lease.web.admin.repository.RoomAttrValueRepository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class AttrValueServiceImpl implements AttrValueService {
    @Autowired
    private AttrValueRepository attrValueRepository;
    @Autowired
    private RoomAttrValueRepository roomAttrValueRepository;

    @Override
    public void saveOrUpdateAttrValue(AttrValue attrValue) {
        if (attrValue.getId() == null) {
            attrValueRepository.save(attrValue);
            return;
        }
        AttrValue existing = attrValueRepository.findById(attrValue.getId())
                .orElseThrow(() -> new RuntimeException("AttrValue not found"));

        existing.setName(attrValue.getName());
        existing.setAttrKeyId(attrValue.getAttrKeyId());
        attrValueRepository.save(existing);
    }

    @Transactional
    @Override
    public boolean deleteAttrValueById(Long id) {
        if (!attrValueRepository.existsById(id)) {
            return false;
        }

        List<RoomAttrValue> roomAttrValues = roomAttrValueRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("attrValueId"), id));
        if (!roomAttrValues.isEmpty()) {
            roomAttrValueRepository.deleteAll(roomAttrValues);
        }

        attrValueRepository.deleteById(id);
        return true;
    }
}
