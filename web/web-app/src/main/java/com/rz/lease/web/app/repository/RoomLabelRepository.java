package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.LabelInfo;
import com.rz.lease.model.entity.RoomLabel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomLabelRepository extends BaseJpaRepository<RoomLabel> {

    @Query("select relation.label from RoomLabel relation where relation.roomId = :roomId")
    List<LabelInfo> findLabelsByRoomId(@Param("roomId") Long roomId);
}
