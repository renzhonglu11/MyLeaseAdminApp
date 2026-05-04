package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.AttrValue;
import com.rz.lease.model.entity.RoomAttrValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomAttrValueRepository extends BaseJpaRepository<RoomAttrValue> {

    @Query("select relation.attrValue from RoomAttrValue relation where relation.roomId = :roomId")
    List<AttrValue> findAttrValuesByRoomId(@Param("roomId") Long roomId);
}
