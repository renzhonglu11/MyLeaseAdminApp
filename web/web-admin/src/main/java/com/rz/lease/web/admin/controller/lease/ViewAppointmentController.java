package com.rz.lease.web.admin.controller.lease;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.enums.AppointmentStatus;
import com.rz.lease.web.admin.service.ViewAppointmentService;
import com.rz.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.rz.lease.web.admin.vo.appointment.AppointmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Appointment viewing management")
@RequestMapping("/admin/appointment")
@RestController
public class ViewAppointmentController {

    private ViewAppointmentService viewAppointmentService;

    public ViewAppointmentController(ViewAppointmentService viewAppointmentService) {
        this.viewAppointmentService = viewAppointmentService;
    }

    @Operation(summary = "Query appointment information by page")
    @GetMapping("page")
    public Result<Page<AppointmentVo>> page(@RequestParam long current, @RequestParam long size,
            AppointmentQueryVo queryVo) {

        return Result.ok(viewAppointmentService.pageItems(current, size, queryVo));
    }

    @Operation(summary = "Update appointment status by ID")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        viewAppointmentService.updateStatusById(id, status);
        return Result.ok();
    }

}
