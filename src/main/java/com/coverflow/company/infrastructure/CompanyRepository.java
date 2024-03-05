package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE c.name LIKE :name " +
            "AND c.companyStatus = 'REGISTRATION' " +
            "ORDER BY c.name ASC")
    Optional<Page<Company>> findAllByNameStartingWithAndStatus(
            final Pageable pageable,
            @Param("name") final String name
    );

    Optional<Company> findByName(final String name);

    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE c.id = :companyId " +
            "AND c.companyStatus = 'REGISTRATION'")
    Optional<Company> findRegisteredCompany(@Param("companyId") final long companyId);

    @Query("SELECT c " +
            "FROM Company c ")
    Optional<Page<Company>> findAllCompanies(final Pageable pageable);

    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE c.companyStatus = :companyStatus")
    Optional<Page<Company>> findAllByStatus(
            final Pageable pageable,
            @Param("companyStatus") final CompanyStatus companyStatus
    );
}
