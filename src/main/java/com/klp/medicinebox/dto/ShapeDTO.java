package com.klp.medicinebox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShapeDTO {
    private String frontPrint; // 약품 텍스트(앞)
    private String backPrint; // 약품 텍스트(뒤)
    private String shape; // 약품 외형
    private String frontColoer; // 약품 색깔(앞)
    private String backColoer; // 약품 색깔(뒤)
    private String frontLine; // 약품 구분선(앞)
    private String backLine; // 약품 구분선(앞)
}
