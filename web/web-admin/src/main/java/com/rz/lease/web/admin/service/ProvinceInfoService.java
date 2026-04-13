package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.ProvinceInfo;

import java.util.List;

/**
 * @author rz
 * @description 针对表【province_info】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface ProvinceInfoService {

    List<ProvinceInfo> listProvinceInfo();
}
