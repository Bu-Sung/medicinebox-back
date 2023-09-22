package com.klp.medicinebox.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="drug")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrugEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "possession_id")
    private Long pid; // 보유 제품 고유번호
    
    @Column(name = "user_id")
    private String uid; // 사용자 아이디
    
    @Column(name = "drug_seq")
    private String seq; // 제품 기준 코드

    @Column(name = "drug_entpname")
    private String entpName; // 업체명

    @Column(name = "drug_name")
    private String name; // 제품 명

    @Column(name = "drug_efcy")
    private String efcy; // 효능

    @Column(name = "drug_use")
    private String use; // 사용법
    
    @Column(name = "drug_atpnWarn")
    private String atpnWarn; // 주의 사항 경고
    
    @Column(name = "drug_atpn")
    private String atpn; // 주의 사항
    
    @Column(name = "drug_intrc")
    private String intrc; // 상호작용
    
    @Column(name = "drug_se")
    private String se; // 부작용
    
    @Column(name = "drug_diposit")
    private String diposit; // 보관법

    @Column(name = "drug_image")
    private String image; // 알약 사진
    
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
