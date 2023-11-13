package com.klp.medicinebox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="dosage")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DosageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dosage_id")
    private Long did; // 섭취 번호
    
    @Column(name = "user_id")
    private String uid; // 사용자 아이디
    
    @Column(name = "dosage_name")
    private String name; // 복용자 이름
    
    @Column(name = "drug_seq")
    private String seq; // 복용 약 제품 코드
    
    @Column(name = "dosage_date")
    private LocalDateTime date; // 섭취 날짜 시간
    
    @Column(name = "dosage_count")
    private int count; // 섭취 갯수
    
    @Column(name = "possession_id")
    private Long pid; // 복용 약 제품 고유번호
    
    @Column(name = "drug_name")
    private String drugName;
}
