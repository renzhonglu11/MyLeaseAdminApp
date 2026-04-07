package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.AttrKey;
import com.rz.lease.web.admin.vo.attr.AttrKeyVo;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【attr_key(房间基本属性表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface AttrKeyService {

    void saveOrUpdateAttrKey(AttrKey attrKey);

    List<AttrKeyVo> listAttrInfo();

    boolean deleteAttrKeyById(Long attrKeyId);
}
