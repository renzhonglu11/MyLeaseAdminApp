package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.rz.lease.model.enums.AppointmentStatus;
import java.util.Date;

@Schema(description = "Viewing appointment information table")
@Entity
@Table(name = "view_appointment")
@Getter
@Setter
public class ViewAppointment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "User id")
    @Column(name = "user_id")
    private Long userId;

    @Schema(description = "User name")
    @Column(name = "name")
    private String name;

    @Schema(description = "User phone number")
    @Column(name = "phone")
    private String phone;

    @Schema(description = "Apartment id")
    @Column(name = "apartment_id")
    private Long apartmentId;

    @Schema(description = "Appointment time")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_time")
    private Date appointmentTime;

    @Schema(description = "Remarks")
    @Column(name = "additional_info")
    private String additionalInfo;

    @Schema(description = "Appointment status")
    @Column(name = "appointment_status")
    private AppointmentStatus appointmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", insertable = false, updatable = false)
    private ApartmentInfo apartment;








}
