package com.rz.lease.web.app.service.impl;

import com.rz.lease.model.entity.LeaseTerm;
import com.rz.lease.web.app.repository.RoomLeaseTermRepository;
import com.rz.lease.web.app.service.LeaseTermService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaseTermServiceImpl implements LeaseTermService {

    private final RoomLeaseTermRepository roomLeaseTermRepository;

    public LeaseTermServiceImpl(RoomLeaseTermRepository roomLeaseTermRepository) {
        this.roomLeaseTermRepository = roomLeaseTermRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaseTerm> listByRoomId(Long id) {
        return roomLeaseTermRepository.findLeaseTermsByRoomId(id);
    }
}
