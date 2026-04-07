package com.rz.lease.web.admin.service;

import java.util.List;

import com.rz.lease.model.entity.DistrictInfo;

/**
 * @author liubo
 * @description 针对表【district_info】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface DistrictInfoService {

    List<DistrictInfo> listDistrictInfoByCityId(Long id);
}
