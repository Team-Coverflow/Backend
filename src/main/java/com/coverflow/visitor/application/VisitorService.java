package com.coverflow.visitor.application;

import com.coverflow.visitor.domain.Visitor;
import com.coverflow.visitor.dto.FindDailyVisitorResponse;
import com.coverflow.visitor.exception.VisitorException;
import com.coverflow.visitor.infrastructure.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    /**
     * [일일 방문자 수 조회 메서드]
     */
    public FindDailyVisitorResponse findDailyCount() {
        final Visitor visitor = visitorRepository.findByToday(LocalDateTime.now())
                .orElseThrow(() -> new VisitorException.DayNotFoundException(LocalDateTime.now()));
        return FindDailyVisitorResponse.of(visitor);
    }

    /**
     * [일일 방문자 수 업데이트 메서드]
     * 오늘 날짜 조회해서 없으면 새로 저장
     * 있으면 카운트 +1
     */
    @Transactional
    public void updateDailyVisitor() {
        log.info(LocalDateTime.now());
        final Visitor visitor = visitorRepository.findByToday(LocalDateTime.now()).orElse(null);
        final Visitor newVisitor = Visitor.builder()
                .today(LocalDateTime.now())
                .count(0)
                .build();

        if (visitor == null) {
            visitorRepository.save(newVisitor);
            return;
        }
        visitor.updateVisitors(visitor.getCount() + 1);
    }
}
