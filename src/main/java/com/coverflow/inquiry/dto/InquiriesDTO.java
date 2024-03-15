package com.coverflow.inquiry.dto;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.domain.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquiriesDTO {
    private long inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;
    private InquiryStatus inquiryStatus;
    private String inquirerNickname;
    private LocalDateTime createdAt;

    public static InquiriesDTO from(final Inquiry inquiry) {
        return new InquiriesDTO(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getInquiryStatus(),
                inquiry.getMember().getNickname(),
                inquiry.getCreatedAt()
        );
    }
}
