package com.rz.lease.web.admin.service;

import java.util.List;

import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.enums.ItemType;

/**
 * @author rz
 * @description 针对表【label_info(标签信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface LabelInfoService {

    List<LabelInfo> listLabelInfo(ItemType type);

    void saveOrUpdateLabelInfo(LabelInfo labelInfo);

    boolean deleteLabelInfoById(Long id);

}
