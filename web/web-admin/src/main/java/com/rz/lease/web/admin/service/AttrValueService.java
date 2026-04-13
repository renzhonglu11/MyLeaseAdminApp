package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.AttrValue;

/**
 * @author rz
 * @description 针对表【attr_value(房间基本属性值表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface AttrValueService {

    void saveOrUpdateAttrValue(AttrValue attrValue);

    boolean deleteAttrValueById(Long id);
}
