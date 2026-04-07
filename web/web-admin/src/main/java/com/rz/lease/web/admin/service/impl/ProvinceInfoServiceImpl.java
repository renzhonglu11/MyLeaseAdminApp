package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ProvinceInfo;
import com.rz.lease.web.admin.service.ProvinceInfoService;
import com.rz.lease.web.admin.repository.ProvinceInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ProvinceInfoServiceImpl implements ProvinceInfoService {
    @Autowired
    private ProvinceInfoRepository provinceInfoRepository;

    @Override
    public List<ProvinceInfo> listProvinceInfo() {
        return provinceInfoRepository.findAll();
    }
}
