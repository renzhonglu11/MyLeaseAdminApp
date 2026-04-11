package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.model.enums.converter.ReleaseStatusConverter;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Room information table")
@Entity
@Table(name = "room_info")
@Getter
@Setter
public class RoomInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Room number")
    @Column(name = "room_number")
    private String roomNumber;

    @Schema(description = "Rent (CNY/month)")
    @Column(name = "rent")
    private BigDecimal rent;

    @Schema(description = "Associated apartment id")
    @Column(name = "apartment_id")
    private Long apartmentId;

    @Schema(description = "Published")
    @Column(name = "is_release")
    @Convert(converter = ReleaseStatusConverter.class)
    private ReleaseStatus isRelease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", insertable = false, updatable = false)
    @JsonIgnore
    private ApartmentInfo apartment;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomAttrValue> roomAttrValues;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomFacility> roomFacilities;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomLabel> roomLabels;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomLeaseTerm> roomLeaseTerms;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomPaymentType> roomPaymentTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<BrowsingHistory> browsingHistories;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<LeaseAgreement> leaseAgreements;











}
