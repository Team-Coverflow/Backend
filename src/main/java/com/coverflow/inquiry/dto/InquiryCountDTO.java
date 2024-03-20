package com.coverflow.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryCountDTO {
    private int allInquiryCount;
    private int waitInquiryCount;
    private int completeInquiryCount;
}
