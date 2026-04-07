package com.rz.lease.web.admin.service.impl;
import com.rz.lease.model.entity.ApartmentFacility;
import com.rz.lease.web.admin.service.ApartmentFacilityService;
import com.rz.lease.web.admin.repository.ApartmentFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* @author liubo
* @description 针对表的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class ApartmentFacilityServiceImpl implements ApartmentFacilityService {
    @Autowired
    private ApartmentFacilityRepository apartmentFacilityRepository;
}
