package com.coverflow.visitor.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_visitor")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 방문자 수 고유 번호
    @Column
    private String today; // 오늘 날짜
    @Column
    private int count; // 방문자 수

    public void updateVisitors(final int count) {
        this.count = count;
    }
}
