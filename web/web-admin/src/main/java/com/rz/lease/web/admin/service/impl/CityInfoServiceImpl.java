package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.CityInfo;
import com.rz.lease.web.admin.repository.CityInfoRepository;
import com.rz.lease.web.admin.service.CityInfoService;
import com.rz.lease.web.admin.vo.apartment.CityInfoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class CityInfoServiceImpl implements CityInfoService {

    private CityInfoRepository cityInfoRepository;

    public CityInfoServiceImpl(CityInfoRepository cityInfoRepository) {
        this.cityInfoRepository = cityInfoRepository;
    }

    @Override
    public List<CityInfoDTO> listCityInfoByProvinceId(Long id) {
        List<CityInfo> cityInfoList = cityInfoRepository.findByProvinceId(id.intValue());
        return cityInfoList.stream()
                .map(cityInfo -> {
                    CityInfoDTO cityInfoDTO = new CityInfoDTO();
                    cityInfoDTO.setId(cityInfo.getId());
                    cityInfoDTO.setName(cityInfo.getName());
                    cityInfoDTO.setProvinceId(cityInfo.getProvinceId());
                    return cityInfoDTO;
                })
                .collect(Collectors.toList());
    }
}
