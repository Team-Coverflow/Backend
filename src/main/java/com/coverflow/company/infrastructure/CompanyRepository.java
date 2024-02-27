package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Page<Company>> findAllCompanies(final Pageable pageable);

    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE c.status = :status")
    Optional<Page<Company>> findAllByStatus(
            final String status,
            final Pageable pageable
    );

    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE c.name LIKE :name " +
            "AND c.status = '등록' " +
            "ORDER BY c.name ASC")
    Optional<Page<Company>> findAllByNameStartingWithAndStatus(
            @Param("name") final String name,
            final Pageable pageable
    );

    Optional<Company> findByName(final String name);

    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE c.id = :companyId " +
            "AND c.status = '등록'")
    Optional<Company> findRegisteredCompany(@Param("companyId") final Long companyId);
}
