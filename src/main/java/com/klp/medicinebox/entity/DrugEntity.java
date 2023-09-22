package com.klp.medicinebox.entity;

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

}
