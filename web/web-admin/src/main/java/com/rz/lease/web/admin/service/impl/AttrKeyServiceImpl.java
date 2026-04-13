package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.AttrKey;
import com.rz.lease.model.entity.AttrValue;
import com.rz.lease.model.entity.RoomAttrValue;
import com.rz.lease.web.admin.service.AttrKeyService;
import com.rz.lease.web.admin.vo.attr.AttrKeyVo;
import com.rz.lease.web.admin.repository.AttrKeyRepository;
import com.rz.lease.web.admin.repository.AttrValueRepository;
import com.rz.lease.web.admin.repository.RoomAttrValueRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class AttrKeyServiceImpl implements AttrKeyService {
    @Autowired
    private AttrKeyRepository attrKeyRepository;
    @Autowired
    private AttrValueRepository attrValueRepository;
    @Autowired
    private RoomAttrValueRepository roomAttrValueRepository;

    @Override
    public void saveOrUpdateAttrKey(AttrKey attrKey) {
        if (attrKey.getId() == null) {
            attrKeyRepository.save(attrKey);
            return;
        }
        AttrKey existing = attrKeyRepository.findById(attrKey.getId())
                .orElseThrow(() -> new RuntimeException("AttrKey not found"));

        existing.setName(attrKey.getName());
        attrKeyRepository.save(existing);
    }

    @Override
    public List<AttrKeyVo> listAttrInfo() {

        // query all AttrKey
        List<AttrKey> attrKeys = attrKeyRepository.findAll();
        // query all AttrValue
        List<AttrValue> attrValues = attrValueRepository.findAll();
        // group AttrValue by attrKeyId
        Map<Long, List<AttrValue>> attrValueMap = attrValues.stream()
                .collect(Collectors.groupingBy(AttrValue::getAttrKeyId));
        // build AttrKeyVo list
        return attrKeys.stream().map(attrKey -> {
            AttrKeyVo attrKeyVo = new AttrKeyVo();
            attrKeyVo.setId(attrKey.getId());
            attrKeyVo.setName(attrKey.getName());
            attrKeyVo.setAttrValueList(attrValueMap.getOrDefault(attrKey.getId(), Collections.emptyList()));
            return attrKeyVo;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteAttrKeyById(Long attrKeyId) {
        if (!attrKeyRepository.existsById(attrKeyId)) {
            return false;
        }
        // Get all AttrValue associated with the AttrKey
        List<AttrValue> attrValues = attrValueRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("attrKeyId"), attrKeyId));
        if (!attrValues.isEmpty()) {
            List<Long> attrValueIds = attrValues.stream()
                    .map(AttrValue::getId)
                    .toList();
            // Get all RoomAttrValue associated with the AttrValue list
            List<RoomAttrValue> roomAttrValues = roomAttrValueRepository.findAll(
                    (root, query, cb) -> root.get("attrValueId").in(attrValueIds));
            // Delete all RoomAttrValue associated with the AttrValue list
            if (!roomAttrValues.isEmpty()) {
                roomAttrValueRepository.deleteAll(roomAttrValues);
            }
            // Delete all AttrValue associated with the AttrKey
            attrValueRepository.deleteAll(attrValues);
        }
        // Delete the AttrKey
        attrKeyRepository.deleteById(attrKeyId);
        return true;
    }
}
