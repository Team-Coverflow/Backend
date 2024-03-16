package com.coverflow.feedback.presentation;


import com.coverflow.feedback.application.FeedbackService;
import com.coverflow.feedback.dto.response.FeedbackResponse;
import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@RestController
public class FeedbackController {

    private FeedbackService feedbackService;

    @GetMapping
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FeedbackResponse>> findFeedback(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FeedbackResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(feedbackService.findFeedback(pageNo, criterion))
                        .build());
    }
}
