package com.isa.entity;

import com.isa.entity.type.BundleType;
import com.isa.entity.type.FlexiType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class ReturnSegment {
    private String returnDate;
    private BundleType bundleType;
    private List<String> inboundFlightNumbers;
    private FlexiType flexiType;
}
