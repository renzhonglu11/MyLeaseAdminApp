package com.rz.lease.web.app.service;

import com.rz.lease.model.entity.LeaseTerm;

import java.util.List;

/**
* @author liubo
* @description 针对表【lease_term(租期)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface LeaseTermService {
    List<LeaseTerm> listByRoomId(Long id);
}
