package com.coverflow.notice.infrastructure;

import com.coverflow.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeCustomRepository {
}