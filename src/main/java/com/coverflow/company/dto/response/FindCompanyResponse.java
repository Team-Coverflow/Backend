package com.coverflow.company.dto.response;

import com.coverflow.company.domain.Company;
import com.coverflow.question.dto.QuestionDTO;

import java.util.List;

public record FindCompanyResponse(
        long companyId,
        String companyName,
        String companyType,
        String companyAddress,
        String companyEstablishment,
        int questionCount,
        int totalPages,
        List<QuestionDTO> questions
) {

    public static FindCompanyResponse of(
            final Company company,
            final int totalPages,
            final List<QuestionDTO> questions
    ) {
        return new FindCompanyResponse(
                company.getId(),
                company.getName(),
                company.getType(),
                company.getCity() + " " + company.getDistrict(),
                company.getEstablishment(),
                company.getQuestionCount(),
                totalPages,
                questions
        );
    }
}
