package com.coverflow.notice.infrastructure;

import com.coverflow.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeCustomRepository {

    Optional<Page<Notice>> find(final Pageable pageable);
}
