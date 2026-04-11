package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rz.lease.model.enums.LeaseSourceType;
import com.rz.lease.model.enums.LeaseStatus;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "Lease agreement information table")
@Entity
@Table(name = "lease_agreement")
@Getter
@Setter
public class LeaseAgreement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Tenant phone number")
    @Column(name = "phone")
    private String phone;

    @Schema(description = "Tenant name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Tenant ID card number")
    @Column(name = "identification_number")
    private String identificationNumber;

    @Schema(description = "Signed apartment id")
    @Column(name = "apartment_id")
    private Long apartmentId;

    @Schema(description = "Signed room id")
    @Column(name = "room_id")
    private Long roomId;

    @Schema(description = "Lease start date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "lease_start_date")
    private Date leaseStartDate;

    @Schema(description = "Lease end date")
    @Temporal(TemporalType.DATE)
    @Column(name = "lease_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date leaseEndDate;

    @Schema(description = "Lease term id")
    @Column(name = "lease_term_id")
    private Long leaseTermId;

    @Schema(description = "Rent (CNY/month)")
    @Column(name = "rent")
    private BigDecimal rent;

    @Schema(description = "Deposit (CNY)")
    @Column(name = "deposit")
    private BigDecimal deposit;

    @Schema(description = "Payment type id")
    @Column(name = "payment_type_id")
    private Long paymentTypeId;

    @Schema(description = "Lease status")
    @Column(name = "status")
    private LeaseStatus status;

    @Schema(description = "Lease source")
    @Column(name = "source_type")
    private LeaseSourceType sourceType;

    @Schema(description = "Remarks")
    @Column(name = "additional_info")
    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", insertable = false, updatable = false)
    @JsonIgnore
    private ApartmentInfo apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    @JsonIgnore
    private RoomInfo room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_term_id", insertable = false, updatable = false)
    @JsonIgnore
    private LeaseTerm leaseTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id", insertable = false, updatable = false)
    @JsonIgnore
    private PaymentType paymentType;

















}
