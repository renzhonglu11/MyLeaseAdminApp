package com.rz.lease.web.admin.service.impl;
import com.rz.lease.model.entity.GraphInfo;
import com.rz.lease.web.admin.service.GraphInfoService;
import com.rz.lease.web.admin.repository.GraphInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* @author liubo
* @description 数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class GraphInfoServiceImpl implements GraphInfoService {
    @Autowired
    private GraphInfoRepository graphInfoRepository;
}
