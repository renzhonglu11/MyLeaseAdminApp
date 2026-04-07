package com.rz.lease.web.admin.vo.apartment;

import com.rz.lease.model.entity.ApartmentInfo;
import com.rz.lease.web.admin.vo.graph.GraphVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "公寓信息")
@Getter
@Setter
public class ApartmentSubmitVo extends ApartmentInfo {

    @Schema(description = "公寓配套id")
    private List<Long> facilityInfoIds;

    @Schema(description = "公寓标签id")
    private List<Long> labelIds;

    @Schema(description = "公寓杂费值id")
    private List<Long> feeValueIds;

    @Schema(description = "公寓图片id")
    private List<GraphVo> graphVoList;

}
