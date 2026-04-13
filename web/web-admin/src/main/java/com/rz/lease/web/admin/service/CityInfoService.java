package com.rz.lease.web.admin.service;

import java.util.List;

import com.rz.lease.web.admin.vo.apartment.CityInfoDTO;

/**
 * @author rz
 * @description 针对表【city_info】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface CityInfoService {

    List<CityInfoDTO> listCityInfoByProvinceId(Long id);
}
