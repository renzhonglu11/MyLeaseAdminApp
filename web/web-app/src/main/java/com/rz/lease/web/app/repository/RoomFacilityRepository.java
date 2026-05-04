package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.FacilityInfo;
import com.rz.lease.model.entity.RoomFacility;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomFacilityRepository extends BaseJpaRepository<RoomFacility> {

    @Query("select relation.facility from RoomFacility relation where relation.roomId = :roomId")
    List<FacilityInfo> findFacilitiesByRoomId(@Param("roomId") Long roomId);
}
