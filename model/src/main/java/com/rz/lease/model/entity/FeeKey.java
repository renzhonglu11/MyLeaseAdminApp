package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Miscellaneous fee name table")
@Entity
@Table(name = "fee_key")
@Getter
@Setter
public class FeeKey extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Payment item key")
    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "feeKey", fetch = FetchType.LAZY)
    private List<FeeValue> feeValues;

}
