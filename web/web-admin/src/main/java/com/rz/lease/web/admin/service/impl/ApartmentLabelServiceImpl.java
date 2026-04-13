package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ApartmentLabel;
import com.rz.lease.web.admin.service.ApartmentLabelService;
import com.rz.lease.web.admin.repository.ApartmentLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentLabelServiceImpl implements ApartmentLabelService {
    @Autowired
    private ApartmentLabelRepository apartmentLabelRepository;
}
