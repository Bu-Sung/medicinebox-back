package com.klp.medicinebox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="possession")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PossessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "possession_id")
    private Long pid; // 보유 제품 고유번호

    @Column(name = "user_id")
    private String uid; // 사용자 아이디

    @Column(name = "drug_seq")
    private String seq; // 제품 기준 코드

    @Column(name = "possession_count")
    private int count; // 보유 갯수

    @Column(name = "possession_buy")
    private LocalDate buyDate; // 구매 날짜

    @Column(name = "possession_expiration")
    private LocalDate expirationDate; // 사용기한

    @Column(name = "possession_register")
    private LocalDateTime registerDate; // 등록 날짜

    @Column(name = "possession_update")
    private LocalDateTime updateDate; // 수정된 날짜
}
