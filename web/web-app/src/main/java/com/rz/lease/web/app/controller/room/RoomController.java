package com.rz.lease.web.app.controller.room;


import com.rz.lease.common.result.Result;
import com.rz.lease.web.app.service.RoomInfoService;
import com.rz.lease.web.app.vo.room.RoomDetailVo;
import com.rz.lease.web.app.vo.room.RoomItemVo;
import com.rz.lease.web.app.vo.room.RoomQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "房间信息")
@RestController
@RequestMapping("/app/room")
public class RoomController {

    @Autowired
    private RoomInfoService service;

    @Operation(summary = "分页查询房间列表")
    @GetMapping("pageItem")
    public Result<Page<RoomItemVo>> pageItem(@RequestParam long current, @RequestParam long size, RoomQueryVo queryVo) {
        PageRequest pageRequest = PageRequest.of(Math.max((int) current - 1, 0), Math.max((int) size, 1));
        Page<RoomItemVo> result = service.pageItem(pageRequest, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据id获取房间的详细信息")
    @GetMapping("getDetailById")
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        RoomDetailVo roomInfo = service.getDetailById(id);
        return Result.ok(roomInfo);
    }

    @Operation(summary = "根据公寓id分页查询房间列表")
    @GetMapping("pageItemByApartmentId")
    public Result<Page<RoomItemVo>> pageItemByApartmentId(@RequestParam long current, @RequestParam long size, @RequestParam Long id) {
        PageRequest pageRequest = PageRequest.of(Math.max((int) current - 1, 0), Math.max((int) size, 1));
        Page<RoomItemVo> result = service.pageItemByApartmentId(pageRequest, id);
        return Result.ok(result);
    }
}
