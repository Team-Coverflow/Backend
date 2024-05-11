package com.coverflow.visitor.application;

import com.coverflow.visitor.domain.Visitor;
import com.coverflow.visitor.dto.response.FindDailyVisitorResponse;
import com.coverflow.visitor.exception.VisitorException;
import com.coverflow.visitor.infrastructure.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Log4j2
@RequiredArgsConstructor
@Service
public class VisitorServiceImpl implements VisitorService {
    private final String NOW = String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate());
    private final VisitorRepository visitorRepository;

    @Override
    @Transactional(readOnly = true)
    public FindDailyVisitorResponse findDailyCount() {
        Visitor visitor = visitorRepository.findByToday(NOW)
                .orElseThrow(() -> new VisitorException.DayNotFoundException(NOW));
        return FindDailyVisitorResponse.from(visitor);
    }

    @Override
    @Transactional
    public void update() {
        log.info(NOW);
        Visitor visitor = visitorRepository.findByToday(NOW).orElse(null);
        Visitor newVisitor = Visitor.builder()
                .today(NOW)
                .count(1)
                .build();

        if (visitor == null) {
            visitorRepository.save(newVisitor);
            return;
        }
        visitor.updateVisitors(visitor.getCount() + 1);
    }
}
