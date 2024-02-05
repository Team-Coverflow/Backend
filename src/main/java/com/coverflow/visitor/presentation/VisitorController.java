package com.coverflow.visitor.presentation;

import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.visitor.application.VisitorService;
import com.coverflow.visitor.dto.FindDailyVisitorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/visitor")
@RestController
public class VisitorController {

    private final VisitorService visitorService;

    @GetMapping("/find-daily-count")
    public ResponseEntity<ResponseHandler<FindDailyVisitorResponse>> findDailyCount() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindDailyVisitorResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("일일 방문자 수 조회를 성공했습니다.")
                        .data(visitorService.findDailyCount())
                        .build()
                );
    }

}
