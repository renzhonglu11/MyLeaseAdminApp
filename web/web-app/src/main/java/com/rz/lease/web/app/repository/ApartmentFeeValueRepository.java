package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.ApartmentFeeValue;
import com.rz.lease.model.entity.FeeValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentFeeValueRepository extends BaseJpaRepository<ApartmentFeeValue> {

    @Query("select relation.feeValue from ApartmentFeeValue relation where relation.apartmentId = :apartmentId")
    List<FeeValue> findFeeValuesByApartmentId(@Param("apartmentId") Long apartmentId);
}
