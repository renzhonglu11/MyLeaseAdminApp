package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.ApartmentFacility;
import com.rz.lease.model.entity.FacilityInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentFacilityRepository extends BaseJpaRepository<ApartmentFacility> {

    @Query("select relation.facility from ApartmentFacility relation where relation.apartmentId = :apartmentId")
    List<FacilityInfo> findFacilitiesByApartmentId(@Param("apartmentId") Long apartmentId);
}
