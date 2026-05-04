package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.ProvinceInfo;
import com.rz.lease.web.app.repository.ProvinceInfoRepository;
import com.rz.lease.web.app.service.ProvinceInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProvinceInfoServiceImpl implements ProvinceInfoService {

    private final ProvinceInfoRepository provinceInfoRepository;

    public ProvinceInfoServiceImpl(ProvinceInfoRepository provinceInfoRepository) {
        this.provinceInfoRepository = provinceInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceInfo> list() {
        return provinceInfoRepository.findAll();
    }
}
