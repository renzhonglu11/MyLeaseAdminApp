package com.rz.lease.web.admin.repository;

import com.rz.lease.model.entity.CityInfo;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【city_info】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.atguigu.lease.model.CityInfo
 */
public interface CityInfoRepository extends BaseJpaRepository<CityInfo> {

    List<CityInfo> findByProvinceId(Integer provinceId);

}
