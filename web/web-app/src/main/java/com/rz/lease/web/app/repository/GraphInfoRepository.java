package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.model.enums.ItemType;

import java.util.List;

public interface GraphInfoRepository extends BaseJpaRepository<GraphInfo> {
    List<GraphInfo> findByItemTypeAndItemId(ItemType itemType, Long itemId);
}
