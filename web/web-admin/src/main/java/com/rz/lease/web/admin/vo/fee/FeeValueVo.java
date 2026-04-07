package com.rz.lease.web.admin.vo.fee;

import com.rz.lease.model.entity.FeeValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "杂费值")
@Getter
@Setter
public class FeeValueVo extends FeeValue {

    @Schema(description = "费用所对的fee_key名称")
    private String feeKeyName;
}
