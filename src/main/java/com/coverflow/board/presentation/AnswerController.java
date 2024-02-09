package com.coverflow.board.presentation;

import com.coverflow.board.application.AnswerService;
import com.coverflow.board.dto.response.FindAnswerResponse;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/answer")
@RestController
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/find-answer")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindAnswerResponse>>> findAnswer(
            final @RequestParam("id") @Valid long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAnswerResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 질문에 대한 전체 답변 조회에 성공했습니다.")
                        .data(answerService.findAnswer(id))
                        .build()
                );
    }
}
