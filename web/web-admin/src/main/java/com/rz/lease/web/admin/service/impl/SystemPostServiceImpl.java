package com.rz.lease.web.admin.service.impl;

import com.rz.lease.model.entity.SystemPost;
import com.rz.lease.web.admin.repository.SystemPostRepository;
import com.rz.lease.web.admin.service.SystemPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rz
 * @description 数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class SystemPostServiceImpl implements SystemPostService {
    @Autowired
    private SystemPostRepository systemPostRepository;

    @Override
    public void saveOrUpdate(SystemPost systemPost) {
        systemPostRepository.save(systemPost);
    }

    @Override
    public Page<SystemPost> page(long current, long size) {
        PageRequest pageRequest = PageRequest.of(
                Math.max((int) current - 1, 0),
                Math.max((int) size, 1),
                Sort.by(Sort.Direction.ASC, "id"));

        return systemPostRepository.findAll(pageRequest);
    }

    @Override
    public SystemPost getById(Long id) {
        return systemPostRepository.findById(id).orElse(null);
    }

    @Override
    public boolean removeById(Long id) {
        if (systemPostRepository.existsById(id)) {
            systemPostRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<SystemPost> list() {
        return systemPostRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
