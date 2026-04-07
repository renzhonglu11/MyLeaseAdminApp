package com.rz.lease.model.entity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Entity
@Table(name = "browsing_history")
@Getter
@Setter
public class BrowsingHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "User id")
    @Column(name = "user_id")
    private Long userId;

    @Schema(description = "Room id")
    @Column(name = "room_id")
    private Long roomId;

    @Schema(description = "Browse time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "browse_time")
    private Date browseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomInfo room;

    public BrowsingHistory() {
    }

    public BrowsingHistory(Long userId, Long roomId, Date browseTime) {
        this.userId = userId;
        this.roomId = roomId;
        this.browseTime = browseTime;
    }




}
