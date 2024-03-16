package com.coverflow.feedback.domain;


import com.coverflow.feedback.dto.request.SaveFeedbackRequest;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_company")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 피드백 고유 번호
    @Column
    private String content; // 피드백 내용

    public Feedback(final SaveFeedbackRequest request) {
        this.content = request.content();
    }
}
