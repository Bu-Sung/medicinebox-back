package com.klp.medicinebox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private String uid; // 사용자 아이디

    @Column(name = "user_pw")
    private String pw; // 사용자 비밀번호

    @Column(name = "user_phone")
    private String phone; // 사용자 전화번호

    @Column(name = "user_gender")
    private int gender; // 사용자 성별

    @Column(name = "user_birth")
    private String birth; // 사용자 생년 월일
    
    @Column(name = "user_del")
    private int del; //사용자 삭제 여부
}
