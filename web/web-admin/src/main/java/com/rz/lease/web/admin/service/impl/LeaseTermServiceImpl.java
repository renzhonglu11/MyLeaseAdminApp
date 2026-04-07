package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.LeaseTerm;
import com.rz.lease.web.admin.repository.LeaseTermRepository;
import com.rz.lease.web.admin.service.LeaseTermService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseTermServiceImpl implements LeaseTermService {

    private final LeaseTermRepository leaseTermRepository;

    public LeaseTermServiceImpl(LeaseTermRepository leaseTermRepository) {
        this.leaseTermRepository = leaseTermRepository;
    }

    @Override
    public List<LeaseTerm> listLeaseTerm() {
        return leaseTermRepository.findAll();
    }

    @Transactional
    @Override
    public void saveOrUpdateLeaseTerm(LeaseTerm leaseTerm) {
        if (leaseTerm.getId() == null) {
            leaseTermRepository.save(leaseTerm);
            return;
        }

        LeaseTerm existing = leaseTermRepository.findById(leaseTerm.getId())
                .orElseThrow(() -> new RuntimeException("LeaseTerm not found"));
        existing.setMonthCount(leaseTerm.getMonthCount());
        existing.setUnit(leaseTerm.getUnit());
    }

    @Override
    public boolean deleteLeaseTermById(Long id) {
        if (!leaseTermRepository.existsById(id)) {
            return false;
        }
        leaseTermRepository.deleteById(id);
        return true;
    }
}
