package com.coverflow.visitor.application;

import com.coverflow.visitor.dto.response.FindDailyVisitorResponse;

public interface VisitorService {

    /**
     * [관리자 전용: 일일 방문자 수 조회 메서드]
     */
    FindDailyVisitorResponse findDailyCount();

    /**
     * [일일 방문자 수 업데이트 메서드]
     * 오늘 날짜 조회해서 없으면 새로 저장
     * 있으면 카운트 +1
     */
    void update();
}
