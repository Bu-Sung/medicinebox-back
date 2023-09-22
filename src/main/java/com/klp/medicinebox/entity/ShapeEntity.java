package com.klp.medicinebox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="shape")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShapeEntity {
    @Id
    @Column(name = "drug_seq")
    private String seq; // 제품 기준 코드

    @Column(name = "drug_frontprint")
    private String frontPrint; // 약품 텍스트(앞)

    @Column(name = "drug_backprint")
    private String backPrint; // 약품 텍스트(뒤)
    
    @Column(name = "drug_shape")
    private String shape; // 약품 외형
    
    @Column(name = "drug_frontcolor")
    private String frontColoer; // 약품 색깔(앞)

    @Column(name = "drug_backcolor")
    private String backColoer; // 약품 색깔(뒤)

    @Column(name = "drug_frontline")
    private String frontLine; // 약품 구분선(앞)
    
    @Column(name = "drug_backline")
    private String backLine; // 약품 구분선(앞)


}
