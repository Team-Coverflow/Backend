package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyCustomRepository {

    Optional<Company> findByName(final String name);

    @Query("""
            SELECT c
            FROM Company c
            JOIN FETCH c.questions
            WHERE c.id = :companyId
            AND c.companyStatus = 'REGISTRATION'
            """)
    Optional<Company> findRegisteredCompany(@Param("companyId") final long companyId);

    @Modifying
    @Query("""
            DELETE FROM Company c
            WHERE c.updatedAt< :date
            AND c.companyStatus = 'DELETION'
            """)
    void deleteByCompanyStatus(@Param("date") final LocalDateTime date);
}
