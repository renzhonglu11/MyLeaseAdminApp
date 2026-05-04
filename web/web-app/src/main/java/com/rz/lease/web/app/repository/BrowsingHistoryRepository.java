package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.BrowsingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BrowsingHistoryRepository extends BaseJpaRepository<BrowsingHistory> {
    Page<BrowsingHistory> findByUserIdOrderByBrowseTimeDesc(Long userId, Pageable pageable);

    Optional<BrowsingHistory> findFirstByUserIdAndRoomId(Long userId, Long roomId);
}
