package com.rz.lease.web.admin.service;

import java.util.List;

import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.enums.ItemType;
/**
* @author liubo
* @description 针对表【facility_info(配套信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface FacilityInfoService {

    List<FacilityInfo> listFacilityInfo(ItemType type);

    void saveOrUpdateFacilityInfo(FacilityInfo facilityInfo);

    boolean deleteFacilityInfoById(Long id);
}
