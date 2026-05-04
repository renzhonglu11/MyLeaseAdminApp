package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.DistrictInfo;

import java.util.List;

public interface DistrictInfoRepository extends BaseJpaRepository<DistrictInfo> {
    List<DistrictInfo> findByCityId(Integer cityId);
}
