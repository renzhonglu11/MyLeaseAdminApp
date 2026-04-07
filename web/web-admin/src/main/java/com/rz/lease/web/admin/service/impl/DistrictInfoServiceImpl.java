package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.DistrictInfo;
import com.rz.lease.web.admin.service.DistrictInfoService;
import com.rz.lease.web.admin.repository.DistrictInfoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class DistrictInfoServiceImpl implements DistrictInfoService {
    @Autowired
    private DistrictInfoRepository districtInfoRepository;

    @Override
    public List<DistrictInfo> listDistrictInfoByCityId(Long id) {

        return districtInfoRepository.findByCityId(id.intValue());
    }
}
