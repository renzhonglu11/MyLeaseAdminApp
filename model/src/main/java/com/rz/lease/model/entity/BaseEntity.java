package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {

    @Schema(description = "Primary key")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Schema(description = "Created time")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    @JsonIgnore
    private Date createTime;

    @Schema(description = "Updated time")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    @JsonIgnore
    private Date updateTime;

    @Schema(description = "Logical deletion")
    @Column(name = "is_deleted")
    @JsonIgnore
    private Byte isDeleted;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        if (createTime == null) {
            createTime = now;
        }
        updateTime = now;
        if (isDeleted == null) {
            isDeleted = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }



}
