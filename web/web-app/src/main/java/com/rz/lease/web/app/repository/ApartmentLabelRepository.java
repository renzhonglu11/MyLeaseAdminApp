package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.ApartmentLabel;
import com.rz.lease.model.entity.LabelInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentLabelRepository extends BaseJpaRepository<ApartmentLabel> {

    @Query("select relation.label from ApartmentLabel relation where relation.apartmentId = :apartmentId")
    List<LabelInfo> findLabelsByApartmentId(@Param("apartmentId") Long apartmentId);
}
