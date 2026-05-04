package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.DistrictInfo;
import com.rz.lease.web.app.repository.DistrictInfoRepository;
import com.rz.lease.web.app.service.DistrictInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DistrictInfoServiceImpl implements DistrictInfoService {

    private final DistrictInfoRepository districtInfoRepository;

    public DistrictInfoServiceImpl(DistrictInfoRepository districtInfoRepository) {
        this.districtInfoRepository = districtInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistrictInfo> listByCityId(Long cityId) {
        return districtInfoRepository.findByCityId(cityId == null ? null : cityId.intValue());
    }
}
