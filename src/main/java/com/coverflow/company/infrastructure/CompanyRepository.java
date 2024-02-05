package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

    Optional<List<Company>> findByNameStartingWith(String name, Pageable pageable);

    Optional<Company> findByName(String name);
}
