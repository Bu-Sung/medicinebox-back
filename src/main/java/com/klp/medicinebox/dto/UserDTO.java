package com.klp.medicinebox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String uid; // 사용자 아이디
    private String pw; // 사용자 비밀번호
    private String phone; // 사용자 전화번호
    private int gender; // 사용자 성별
    private String birth; // 사용자 생년 월일
}
