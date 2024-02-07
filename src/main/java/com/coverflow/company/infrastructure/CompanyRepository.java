package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<List<Company>> findByNameStartingWith(String name, Pageable pageable);

    Optional<Company> findByName(String name);

    @Query(value = "SELECT c FROM Company c WHERE c.name LIKE :name ORDER BY c.name ASC")
    Optional<List<Company>> findAllCompaniesStartingWithName(@Param("name") String name);
}
