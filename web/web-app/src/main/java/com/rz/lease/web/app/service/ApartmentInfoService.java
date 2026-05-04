package com.rz.lease.web.app.service;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.rz.lease.web.app.vo.apartment.ApartmentItemVo;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
 * @createDate 2023-07-26 11:12:39
 */
public interface ApartmentInfoService {
    ApartmentItemVo selectApartmentItemVoById(Long id);

    ApartmentDetailVo getApartmentDetailById(Long id);
}
