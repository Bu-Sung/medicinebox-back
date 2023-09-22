package com.klp.medicinebox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrugDTO {
    private String seq; // 제품 기준 코드
    private String entpName; // 업체명
    private String name; // 제품 명
    private String efcy; // 효능
    private String use; // 사용법
    private String atpnWarn; // 주의 사항 경고
    private String atpn; // 주의 사항
    private String intrc; // 상호작용
    private String se; // 부작용
    private String diposit; // 보관법
    private String image; // 알약 사진
    private int count; // 수량
    private String buyDate; // 구매 날짜
    private String expirationDate; // 사용기한
}
