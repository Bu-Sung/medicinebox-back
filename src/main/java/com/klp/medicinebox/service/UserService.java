package com.klp.medicinebox.service;

import com.klp.medicinebox.dto.UserDTO;
import com.klp.medicinebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 사용자 로그인 함수
     * @param userDTO 사용자 로그인 정보
     * @return 로그인 성공 여부를 반환
     * @author 이수진
     *
     */
    public boolean login(UserDTO userDTO){
        // 1. 사용자 아이디로 사용자 정보 받아오기
        // 2. 사용자 정보가 있는지 확인
        // 3. 사용자 정보가 있으면 비밀번호를 비교
        // 4. 모두 일치 시 true, 하나라도 틀리면 false
        return false;
    }

    /**
     * 사용자 회원 가입 함수
     * @param userDTO 사용자 회원가입 정보
     * @return 회원 가입 성공 여부를 반환
     * @author 이수진
     *
     */
    public boolean signUp(UserDTO userDTO){
        // 1. 사용자로부터 받아온 DTO 정보를 Entity로 변경
        // 2. 정보를 save하기
        // 3. save에 성공하면 Entity 객체가 return되므로 null 유무로 성공, 실패를 판단
        return false;
    }

    /**
     * 사용자 정보를 받아오는 함수
     * @param uid 받아올 사용자 아이디
     * @return 사용자 정보 데이터
     * @author 이수진
     *
     */
    public UserDTO getUserInfo(String uid){
        // 1. 사용자 정보 가져오기
        // 2. DTO로 변환하여 반환
        return null;
    }

    /**
     * 사용자가 회원가입을 할 시 아이디 중복을 확인하는 함수
     * @param id 사용자가 아이디 중복 확인을 위해 입력한 아이디
     * @return 존재 여부를 반환
     * @author 이수진
     *
     */
    public boolean checkId(String id){
        // 1. 사용자가 입력한 아이디가 이미 존재하는 지 확인
        // 2. 결과에 따라 있으면 true, 없으면 false
        return false;
    }

    /**
     * 사용자 정보 변경을 위한 함수
     * @param userDTO 사용자 정보 변경을 위한 입력 정보
     * @return 변경 성공 여부를 반환
     * @author 이수진
     *
     */
    public boolean update(UserDTO userDTO){
        // 1. 사용자가 입력한 정보를 DTO에서 Entity로 변경
        // 2. 입력과 똑같이 save함수를 사용하여 결과 값 반환
        return false;
    }

    /**
     * 사용자 아이디를 삭제하는 함수
     * @param uid 삭제할 사용자 아이디
     * @return 사용자 삭제 여부를 반환
     * @author 이수진
     *
     */
    public boolean delete(String uid){
        // 1. 사용자 아이디의 존재 여부를 확인
        // 2. 사용자 아이디 삭제
        // 3. 존재 여부에서 존재하지 않은면 false, 아이디 삭제하면 trues
        return false;
    }

    /**
     * 사용자 아이디를 찾기 위한 함수
     * @param userDTO 아이디 찾기의 대한 입력 정보
     * @return 찾은 아이디의 일부 이거나 정보 불일치 메시지
     * @author 이수진
     *
     */
    public String getUserId(UserDTO userDTO){
        // 1. 입력 받은 이름과 전화번호로 사용자 검색
        // 2. 아이디 존재 시 일정 부분을 *로 변경
        // 3. 아이디 미 존재 시 "일치하는 정보가 없습니다." 반환
        return "kbs*****";
    }

    /**
     * 로그인 전 사용자 비밀번호 변경을 위한 사용자 확인 함수 
     * @param userDTO 사용자 확인을 위한 입력 정보
     * @return 사용자 일치 여부
     * @author 이수진
     *
     */
    public boolean getUserPw(UserDTO userDTO){
        // 1. 입력 받은 아이디와 전화번호로 사용자 검색
        // 2. 일치하는 사용자가 있으면 true 없으면 false;
        return false;
    }

    /**
     * 사용자 비밀번호 변경을 위한 함수
     * @param userDTO 변경할 비밀번호 입력 정보
     * @return 변경 성공 여부
     * @author 이수진
     *
     */
    public boolean setUserPw(UserDTO userDTO){
        // 1. 유효성 검사는 front에서 진행함으로 받은 비밀번호 바로 변경
        // 2. 변경 성공 여부를 반확
        return false;
    }

}
