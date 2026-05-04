package com.rz.lease.web.app.service;

import com.rz.lease.model.entity.BrowsingHistory;
import com.rz.lease.web.app.vo.history.HistoryItemVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
* @author liubo
* @description 针对表【browsing_history(浏览历史)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface BrowsingHistoryService {
    Page<HistoryItemVo> pageItemByUserId(Pageable pageable, Long userId);

    void saveHistory(Long userId, Long roomId);
}
