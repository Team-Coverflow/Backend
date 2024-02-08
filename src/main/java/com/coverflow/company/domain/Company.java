package com.coverflow.company.domain;

import com.coverflow.board.domain.Question;
import com.coverflow.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_company")
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String type;
    @Column
    private String city;
    @Column
    private String district;
    @Column
    private String establishment;
    @Column
    private String status;

    @OneToMany(mappedBy = "company")
    private List<Question> questions = new ArrayList<>();

    public void updateCompany(final Company company) {
        this.name = company.getName();
        this.type = company.getType();
        this.city = company.getCity();
        this.district = company.getDistrict();
        this.establishment = company.getEstablishment();
    }

    public void updateStatus(final String status) {
        this.status = status;
    }
}
