package com.coverflow.inquiry.dto;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.domain.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDTO {
    private long inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
    private InquiryStatus inquiryStatus;
    private String inquirerNickname;
    private LocalDate createdAt;
    private int allInquiriesCount;
    private int waitInquiriesCount;
    private int completeInquiriesCount;

    public static InquiryDTO of(
            final Inquiry inquiry,
            final InquiryCountDTO inquiryCountDTO
    ) {
        return new InquiryDTO(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getInquiryStatus(),
                inquiry.getMember().getNickname(),
                inquiry.getCreatedAt().toLocalDate(),
                inquiryCountDTO.getAllInquiryCount(),
                inquiryCountDTO.getWaitInquiryCount(),
                inquiryCountDTO.getCompleteInquiryCount()
        );
    }
}
