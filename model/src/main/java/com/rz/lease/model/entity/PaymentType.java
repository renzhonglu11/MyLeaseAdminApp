package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Payment type table")
@Entity
@Table(name = "payment_type")
@Getter
@Setter
public class PaymentType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Payment method name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Lease term count per payment")
    @Column(name = "pay_month_count")
    private String payMonthCount;

    @Schema(description = "Payment description")
    @Column(name = "additional_info")
    private String additionalInfo;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY)
    private List<RoomPaymentType> roomPaymentTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY)
    private List<LeaseAgreement> leaseAgreements;




}
