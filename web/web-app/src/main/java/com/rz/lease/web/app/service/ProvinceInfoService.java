package com.rz.lease.web.app.service;

import com.rz.lease.model.entity.ProvinceInfo;
import java.util.List;

/**
* @author liubo
* @description 针对表【province_info】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface ProvinceInfoService {
    List<ProvinceInfo> list();
}
