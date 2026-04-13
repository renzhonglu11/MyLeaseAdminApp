package com.rz.lease.web.admin.service;

import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.web.admin.vo.room.RoomDetailVo;
import com.rz.lease.web.admin.vo.room.RoomItemVo;
import com.rz.lease.web.admin.vo.room.RoomQueryVo;
import com.rz.lease.web.admin.vo.room.RoomSubmitVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author rz
 * @description 针对表【room_info(房间信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface RoomInfoService {
    void saveOrUpdateRoomInfo(RoomSubmitVo roomSubmitVo);

    Page<RoomItemVo> pageItem(long current, long size, RoomQueryVo queryVo);

    RoomDetailVo getDetailById(Long id);

    void removeRoomById(Long id);

    void updateReleaseStatusById(Long id, ReleaseStatus status);

    List<RoomInfo> listBasicByApartmentId(Long id);
}
