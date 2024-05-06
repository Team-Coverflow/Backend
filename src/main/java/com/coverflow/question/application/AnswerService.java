package com.coverflow.question.application;

import com.coverflow.question.dto.AnswerListDTO;
import com.coverflow.question.dto.request.FindAnswerAdminRequest;
import com.coverflow.question.dto.request.SaveAnswerRequest;
import com.coverflow.question.dto.request.UpdateAnswerRequest;
import com.coverflow.question.dto.request.UpdateSelectionRequest;
import com.coverflow.question.dto.response.FindAnswerResponse;
import com.coverflow.question.dto.response.FindMyAnswersResponse;

import java.util.UUID;

public interface AnswerService {

    /**
     * [특정 질문에 대한 답변 조회 메서드]
     */
    AnswerListDTO findByQuestionId(
            final int pageNo,
            final String criterion,
            final long questionId
    );

    /**
     * [내 답변 목록 조회 메서드]
     */
    FindMyAnswersResponse findByMemberId(
            final int pageNo,
            final String criterion,
            final UUID memberId
    );

    /**
     * [관리자 - 답변 조회 메서드]
     */
    FindAnswerResponse find(
            final int pageNo,
            final String criterion,
            final FindAnswerAdminRequest request
    );

    /**
     * [답변 등록 메서드]
     */
    void save(final SaveAnswerRequest request, final String memberId);

    /**
     * [답변 채택 메서드]
     */
    void choose(
            final long answerId,
            final UpdateSelectionRequest request,
            final String memberId
    );

    /**
     * [관리자 - 답변 수정 메서드]
     */
    void update(final long answerId, final UpdateAnswerRequest request);

    /**
     * [관리자 - 답변 삭제 메서드]
     */
    void delete(final long answerId);
}
