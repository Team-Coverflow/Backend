package com.coverflow.board.presentation;

import com.coverflow.board.application.QuestionService;
import com.coverflow.board.dto.response.QuestionResponse;
import com.coverflow.global.annotation.AdminAuthorize;
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
@RequestMapping("/api/question")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/find-question")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<QuestionResponse>> findQuestionById(
            @RequestParam("id") @Valid long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<QuestionResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 질문 글 조회에 성공했습니다.")
                        .data(questionService.findQuestionById(id))
                        .build()
                );
    }

    @GetMapping("/find-all-questions")
    public ResponseEntity<ResponseHandler<List<QuestionResponse>>> findAllQuestionsByCompanyId(
            @RequestParam("id") @Valid long id
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<QuestionResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회사의 전체 질문 글 조회에 성공했습니다.")
                        .data(questionService.findAllQuestionsByCompanyId(id))
                        .build()
                );
    }

    @GetMapping("/all-questions")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<QuestionResponse>>> findAllQuestions() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<QuestionResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("전체 질문 글 조회에 성공했습니다.")
                        .data(questionService.findAllQuestions())
                        .build()
                );
    }
}
