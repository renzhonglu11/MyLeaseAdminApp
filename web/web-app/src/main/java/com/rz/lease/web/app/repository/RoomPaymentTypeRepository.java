package com.rz.lease.web.app.repository;

import com.rz.lease.model.entity.PaymentType;
import com.rz.lease.model.entity.RoomPaymentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomPaymentTypeRepository extends BaseJpaRepository<RoomPaymentType> {

    @Query("select relation.paymentType from RoomPaymentType relation where relation.roomId = :roomId")
    List<PaymentType> findPaymentTypesByRoomId(@Param("roomId") Long roomId);

    @Query("select relation.roomId from RoomPaymentType relation where relation.paymentTypeId = :paymentTypeId")
    List<Long> findRoomIdsByPaymentTypeId(@Param("paymentTypeId") Long paymentTypeId);
}
