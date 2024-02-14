package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;
import com.coverflow.question.dto.QuestionDTO;

import java.util.List;
import java.util.stream.Collectors;

public record FindCompanyResponse(
        Long companyId,
        String companyName,
        String type,
        String address,
        String establishment,
        int questionCount,
        List<QuestionDTO> questions
) {

    public static FindCompanyResponse from(final Company company) {
        return new FindCompanyResponse(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment(),
                company.getQuestionCount(),
                company.getQuestions().stream()
                        .map(question -> new QuestionDTO(
                                question.getMember().getNickname(),
                                question.getMember().getTag(),
                                question.getTitle(),
                                question.getContent(),
                                question.getViewCount(),
                                question.getAnswerCount(),
                                question.getCreatedAt()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
