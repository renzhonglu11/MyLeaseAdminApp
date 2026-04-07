package com.rz.lease.web.admin.repository;

import java.util.List;

import com.rz.lease.model.entity.DistrictInfo;

/**
 * @author liubo
 * @description 针对表【district_info】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.atguigu.lease.model.DistrictInfo
 */
public interface DistrictInfoRepository extends BaseJpaRepository<DistrictInfo> {

    List<DistrictInfo> findByCityId(Integer cityId);
}
