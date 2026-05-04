package com.rz.lease.web.app.controller.history;


import com.rz.lease.common.login.LoginUserHolder;
import com.rz.lease.common.result.Result;
import com.rz.lease.web.app.service.BrowsingHistoryService;
import com.rz.lease.web.app.vo.history.HistoryItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "浏览历史管理")
@RequestMapping("/app/history")
public class BrowsingHistoryController {

    @Autowired
    private BrowsingHistoryService service;

    @Operation(summary = "获取浏览历史")
    @GetMapping("pageItem")
    private Result<Page<HistoryItemVo>> page(@RequestParam long current, @RequestParam long size) {
        PageRequest pageRequest = PageRequest.of(Math.max((int) current - 1, 0), Math.max((int) size, 1));
        Page<HistoryItemVo> result = service.pageItemByUserId(pageRequest, LoginUserHolder.getLoginUser().getUserId());
        return Result.ok(result);
    }
}
