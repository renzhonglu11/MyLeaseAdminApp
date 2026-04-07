package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Miscellaneous fee value table")
@Entity
@Table(name = "fee_value")
@Getter
@Setter
public class FeeValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Fee value")
    @Column(name = "name")
    private String name;

    @Schema(description = "Charge unit")
    @Column(name = "unit")
    private String unit;

    @Schema(description = "Fee key code corresponding to this fee")
    @Column(name = "fee_key_id")
    private Long feeKeyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_key_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private FeeKey feeKey;

    @JsonIgnore
    @OneToMany(mappedBy = "feeValue", fetch = FetchType.LAZY)
    private List<ApartmentFeeValue> apartmentFeeValues;




}
