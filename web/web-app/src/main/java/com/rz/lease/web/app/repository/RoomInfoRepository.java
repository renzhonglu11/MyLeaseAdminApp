package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.RoomInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface RoomInfoRepository extends BaseJpaRepository<RoomInfo> {

    @Query("select min(room.rent) from RoomInfo room where room.apartmentId = :apartmentId and (room.isDeleted = 0 or room.isDeleted is null)")
    BigDecimal findMinRentByApartmentId(@Param("apartmentId") Long apartmentId);
}
