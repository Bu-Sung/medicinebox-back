/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.klp.medicinebox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MixtureDTO {
    private String drugSeq;  // 제품기준코드A
    private String drugName;  // 제품 이름A
    private String drugEntp;  // 업체명A
    private String mixtureSeq;  // 제품기준코드B
    private String mixtureName;  // 제품 이름B
    private String mixtureEntp;  // 업체명B
    private String prohibitContent;  // 금기 내용 
    private Long did; // 섭취 번호
}
