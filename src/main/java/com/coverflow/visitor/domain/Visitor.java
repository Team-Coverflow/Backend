package com.coverflow.visitor.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_visitor")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private LocalDateTime today;
    @Column
    private int count;

    public void updateVisitors(final int count) {
        this.count = count;
    }

    public void updateToday(final LocalDateTime today, final int count) {
        this.today = today;
        this.count = count;
    }
}
