package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.LeaseTerm;
import com.rz.lease.model.entity.RoomLeaseTerm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomLeaseTermRepository extends BaseJpaRepository<RoomLeaseTerm> {

    @Query("select relation.leaseTerm from RoomLeaseTerm relation where relation.roomId = :roomId")
    List<LeaseTerm> findLeaseTermsByRoomId(@Param("roomId") Long roomId);
}
