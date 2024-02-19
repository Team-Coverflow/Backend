package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;
import com.coverflow.question.dto.QuestionDTO;

import java.util.List;

public record FindCompanyResponse(
        Long companyId,
        String companyName,
        String type,
        String address,
        String establishment,
        int questionCount,
        List<QuestionDTO> questions
) {

    public static FindCompanyResponse of(
            final Company company,
            final List<QuestionDTO> questions) {
        return new FindCompanyResponse(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment(),
                company.getQuestionCount(),
                questions
        );
    }
}
