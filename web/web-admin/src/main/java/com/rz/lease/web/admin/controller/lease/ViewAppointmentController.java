package com.rz.lease.web.admin.controller.lease;

import com.rz.lease.common.result.Result;
import com.rz.lease.model.enums.AppointmentStatus;
import com.rz.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.rz.lease.web.admin.vo.appointment.AppointmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Appointment viewing management")
@RequestMapping("/admin/appointment")
@RestController
public class ViewAppointmentController {

    @Operation(summary = "Query appointment information by page")
    @GetMapping("page")
    public Result<List<AppointmentVo>> page(@RequestParam long current, @RequestParam long size,
            AppointmentQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "Update appointment status by ID")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        return Result.ok();
    }

}
