package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.SystemPost;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author rz
 * @description 针对表【system_post(岗位信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface SystemPostService {
    void saveOrUpdate(SystemPost systemPost);

    Page<SystemPost> page(long current, long size);

    SystemPost getById(Long id);

    boolean removeById(Long id);

    List<SystemPost> list();
}
