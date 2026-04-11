package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.model.enums.converter.ReleaseStatusConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Apartment information table")
@Entity
@Table(name = "apartment_info")
@Getter
@Setter
public class ApartmentInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Apartment name")
    @Column(name = "name")
    private String name;

    @Schema(description = "Apartment description")
    @Column(name = "introduction")
    private String introduction;

    @Schema(description = "District id")
    @Column(name = "district_id")
    private Long districtId;

    @Schema(description = "District name")
    @Column(name = "district_name")
    private String districtName;

    @Schema(description = "City id")
    @Column(name = "city_id")
    private Long cityId;

    @Schema(description = "City name")
    @Column(name = "city_name")
    private String cityName;

    @Schema(description = "Province id")
    @Column(name = "province_id")
    private Long provinceId;

    @Schema(description = "District name")
    @Column(name = "province_name")
    private String provinceName;

    @Schema(description = "Detailed address")
    @Column(name = "address_detail")
    private String addressDetail;

    @Schema(description = "Longitude")
    @Column(name = "latitude")
    private String latitude;

    @Schema(description = "Latitude")
    @Column(name = "longitude")
    private String longitude;

    @Schema(description = "Apartment front desk phone")
    @Column(name = "phone")
    private String phone;

    @Schema(description = "Published")
    @Column(name = "is_release")
    @Convert(converter = ReleaseStatusConverter.class)
    private ReleaseStatus isRelease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", insertable = false, updatable = false)
    @JsonIgnore
    private DistrictInfo district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    @JsonIgnore
    private CityInfo city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProvinceInfo province;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<RoomInfo> rooms;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<ApartmentFacility> apartmentFacilities;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<ApartmentFeeValue> apartmentFeeValues;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<ApartmentLabel> apartmentLabels;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<LeaseAgreement> leaseAgreements;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    private List<ViewAppointment> viewAppointments;






















}
