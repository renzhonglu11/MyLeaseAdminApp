package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.RoomAttrValue;
import com.rz.lease.web.admin.service.RoomAttrValueService;
import com.rz.lease.web.admin.repository.RoomAttrValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomAttrValueServiceImpl implements RoomAttrValueService {
    @Autowired
    private RoomAttrValueRepository roomAttrValueRepository;
}
