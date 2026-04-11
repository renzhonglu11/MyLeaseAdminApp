package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.entity.RoomInfo;
import com.rz.lease.model.enums.ReleaseStatus;
import com.rz.lease.web.admin.service.RoomInfoService;
import com.rz.lease.web.admin.vo.room.RoomDetailVo;
import com.rz.lease.web.admin.vo.room.RoomItemVo;
import com.rz.lease.web.admin.vo.room.RoomQueryVo;
import com.rz.lease.web.admin.vo.room.RoomSubmitVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Room information management")
@RestController
@RequestMapping("/admin/room")
public class RoomController {

    private final RoomInfoService roomInfoService;

    public RoomController(RoomInfoService roomInfoService) {
        this.roomInfoService = roomInfoService;
    }

    @Operation(summary = "Save or update room information")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody RoomSubmitVo roomSubmitVo) {
        roomInfoService.saveOrUpdateRoomInfo(roomSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "Query room list by page according to conditions")
    @GetMapping("pageItem")
    public Result<Page<RoomItemVo>> pageItem(@RequestParam long current, @RequestParam long size, RoomQueryVo queryVo) {
        return Result.ok(roomInfoService.pageItem(current, size, queryVo));
    }

    @Operation(summary = "Get room details by ID")
    @GetMapping("getDetailById")
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        RoomDetailVo detailVo = roomInfoService.getDetailById(id);
        return Result.ok(detailVo);
    }

    @Operation(summary = "Delete room information by ID")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        roomInfoService.removeRoomById(id);
        return Result.ok();
    }

    @Operation(summary = "Update room release status by ID")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        roomInfoService.updateReleaseStatusById(id, status);
        return Result.ok();
    }

    @GetMapping("listBasicByApartmentId")
    @Operation(summary = "Query room list by apartment ID")
    public Result<List<RoomInfo>> listBasicByApartmentId(@RequestParam Long id) {
        return Result.ok(roomInfoService.listBasicByApartmentId(id));
    }

}
