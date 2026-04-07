package com.rz.lease.model.enums.converter;

import com.rz.lease.model.enums.ItemType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ItemTypeConverter extends BaseEnumConverter<ItemType> {

    public ItemTypeConverter() {
        super(ItemType.class);
    }
}
