package com.rz.lease.web.admin.vo.attr;

import com.rz.lease.model.entity.AttrKey;
import com.rz.lease.model.entity.AttrValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttrKeyVo extends AttrKey {

    @Schema(description = "attribute value list")
    private List<AttrValue> attrValueList;
}
