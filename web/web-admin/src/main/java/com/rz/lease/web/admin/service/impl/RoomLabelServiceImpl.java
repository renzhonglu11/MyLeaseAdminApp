package com.rz.lease.web.admin.service.impl;
import com.rz.lease.model.entity.RoomLabel;
import com.rz.lease.web.admin.service.RoomLabelService;
import com.rz.lease.web.admin.repository.RoomLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* @author liubo
* @description 数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class RoomLabelServiceImpl implements RoomLabelService {
    @Autowired
    private RoomLabelRepository roomLabelRepository;
}
