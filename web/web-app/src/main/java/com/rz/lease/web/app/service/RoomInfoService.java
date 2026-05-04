package com.rz.lease.web.app.service;

import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.web.app.vo.room.RoomDetailVo;
import com.rz.lease.web.app.vo.room.RoomItemVo;
import com.rz.lease.web.app.vo.room.RoomQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface RoomInfoService {
    Page<RoomItemVo> pageItem(Pageable pageable, RoomQueryVo queryVo);

    Page<RoomItemVo> pageItemByApartmentId(Pageable pageable, Long id);

    RoomDetailVo getDetailById(Long id);
}
