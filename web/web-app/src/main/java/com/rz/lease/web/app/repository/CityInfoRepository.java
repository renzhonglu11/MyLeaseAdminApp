package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.CityInfo;

import java.util.List;

public interface CityInfoRepository extends BaseJpaRepository<CityInfo> {
    List<CityInfo> findByProvinceId(Integer provinceId);
}
