package com.klp.medicinebox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DosageDTO {
    private Long did; // 섭취 번호
    private String userName; // 복용자 이름
    private String drugName; // 약품 이름
    private String seq; // 복용 약 제품 코드
    private LocalDateTime date; // 섭취 날짜 시간
    private int count; // 섭취 갯수
}
