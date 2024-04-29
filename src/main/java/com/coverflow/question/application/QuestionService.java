package com.coverflow.question.application;

import com.coverflow.question.dto.CompanyAndQuestionDTO;
import com.coverflow.question.dto.request.FindQuestionAdminRequest;
import com.coverflow.question.dto.request.SaveQuestionRequest;
import com.coverflow.question.dto.request.UpdateQuestionRequest;
import com.coverflow.question.dto.response.FindAllQuestionsResponse;
import com.coverflow.question.dto.response.FindMyQuestionsResponse;
import com.coverflow.question.dto.response.FindQuestionResponse;

import java.util.UUID;

public interface QuestionService {

    /**
     * [특정 기업의 질문 조회 메서드]
     * 기업 id로 기업 및 질문 조회
     */
    CompanyAndQuestionDTO findByCompanyId(
            final int pageNo,
            final String criterion,
            final long companyId,
            final String questionTag
    );

    /**
     * [내 질문 조회 메서드]
     * 회원 id로 조회
     */
    FindMyQuestionsResponse findByMemberId(
            final int pageNo,
            final String criterion,
            final UUID memberId
    );

    /**
     * [특정 질문과 답변 조회 메서드]
     * 특정 질문 id로 질문 및 답변 조회
     */
    FindQuestionResponse findById(
            final int pageNo,
            final String criterion,
            final long questionId
    );

    /**
     * [관리자 - 질문 조회 메서드]
     */
    FindAllQuestionsResponse find(
            final int pageNo,
            final String criterion,
            final FindQuestionAdminRequest request
    );

    /**
     * [질문 등록 메서드]
     */
    void save(final SaveQuestionRequest request, final String memberId);

    /**
     * [질문 수정 메서드]
     */
    void update(final long questionId, final UpdateQuestionRequest request);

    /**
     * [관리자 - 질문 삭제 메서드]
     */
    void delete(final long questionId);
}
