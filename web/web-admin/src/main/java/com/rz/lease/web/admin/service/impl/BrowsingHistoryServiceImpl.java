package com.rz.lease.web.admin.service.impl;
import com.rz.lease.model.entity.BrowsingHistory;
import com.rz.lease.web.admin.service.BrowsingHistoryService;
import com.rz.lease.web.admin.repository.BrowsingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
* @author liubo
* @description 数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {
    @Autowired
    private BrowsingHistoryRepository browsingHistoryRepository;
}
