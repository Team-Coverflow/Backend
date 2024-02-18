package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<List<Company>> findByNameStartingWithAndStatus(
            final String name,
            final Pageable pageable,
            final String status
    );

    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE c.name LIKE :name " +
            "AND c.status = :status " +
            "ORDER BY c.name ASC")
    Optional<List<Company>> findAllCompaniesStartingWithNameAndStatus(
            @Param("name") final String name,
            @Param("status") final String status
    );

    @Query("SELECT c " +
            "FROM Company c " +
            "ORDER BY c.name ASC")
    Optional<List<Company>> findAllCompanies();

    Optional<Company> findByName(final String name);

    @Query("SELECT c " +
            "FROM Company c " +
            "LEFT JOIN FETCH c.questions q " +
            "WHERE c.id = :companyId " +
            "AND c.status = '등록' " +
            "AND q.status = '등록' " +
            "ORDER BY q.createdAt DESC")
    Optional<Company> findByCompanyIdWithQuestion(@Param("companyId") final Long companyId);

    Optional<List<Company>> findByStatus(final String status);
}
