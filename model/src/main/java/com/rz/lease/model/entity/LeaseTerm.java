package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Lease term information")
@Entity
@Table(name = "lease_term")
@Getter
@Setter
public class LeaseTerm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Lease term months")
    @Column(name = "month_count")
    private Integer monthCount;

    @Schema(description = "Lease term unit: month")
    @Column(name = "unit")
    private String unit;

    @JsonIgnore
    @OneToMany(mappedBy = "leaseTerm", fetch = FetchType.LAZY)
    private List<RoomLeaseTerm> roomLeaseTerms;

    @JsonIgnore
    @OneToMany(mappedBy = "leaseTerm", fetch = FetchType.LAZY)
    private List<LeaseAgreement> leaseAgreements;



}
