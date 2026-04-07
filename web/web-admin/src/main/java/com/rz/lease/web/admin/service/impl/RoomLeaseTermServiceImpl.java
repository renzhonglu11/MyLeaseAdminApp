package com.rz.lease.web.admin.service.impl;
import com.rz.lease.model.entity.RoomLeaseTerm;
import com.rz.lease.web.admin.service.RoomLeaseTermService;
import com.rz.lease.web.admin.repository.RoomLeaseTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* @author liubo
* @description 数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class RoomLeaseTermServiceImpl implements RoomLeaseTermService {
    @Autowired
    private RoomLeaseTermRepository roomLeaseTermRepository;
}
