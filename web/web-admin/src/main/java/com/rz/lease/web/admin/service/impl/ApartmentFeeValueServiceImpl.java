package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.ApartmentFeeValue;
import com.rz.lease.web.admin.service.ApartmentFeeValueService;
import com.rz.lease.web.admin.repository.ApartmentFeeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentFeeValueServiceImpl implements ApartmentFeeValueService {
    @Autowired
    private ApartmentFeeValueRepository apartmentFeeValueRepository;
}
