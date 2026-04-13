package com.rz.lease.web.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.rz.lease.web.admin.vo.apartment.ApartmentSubmitVo;

/**
 * @author rz
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface ApartmentInfoService {
    void saveOrUpdateApartmentInfo(ApartmentSubmitVo apartmentSubmitVo);

    Page<ApartmentItemVo> pageItem(long current, long size, ApartmentQueryVo queryVo);

    ApartmentDetailVo getDetailById(Long id);

    void removeApartmentById(Long id);

    void updateReleaseStatusById(Long id, ReleaseStatus status);

    List<ApartmentItemVo> listInfoByDistrictId(Long id);
}
