package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.CityInfo;
import com.rz.lease.web.app.repository.CityInfoRepository;
import com.rz.lease.web.app.service.CityInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityInfoServiceImpl implements CityInfoService {

    private final CityInfoRepository cityInfoRepository;

    public CityInfoServiceImpl(CityInfoRepository cityInfoRepository) {
        this.cityInfoRepository = cityInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityInfo> listByProvinceId(Long provinceId) {
        return cityInfoRepository.findByProvinceId(provinceId == null ? null : provinceId.intValue());
    }
}
