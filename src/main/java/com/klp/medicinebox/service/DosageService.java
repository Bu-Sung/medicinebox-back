package com.klp.medicinebox.service;

import com.klp.medicinebox.dto.DosageDTO;
import com.klp.medicinebox.repository.DosageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DosageService {

    private final DosageRepository dosageRepository;

    /**
     * 자신의 복약 기록을 가져오는 함수
     * @param uid 사용자 아이디
     * @return 사용자 복약 리스트 정보
     * @author 박채빈
     *
     */
    public List<DosageDTO> getDosageList(String uid){
        List<DosageDTO> dosageDTOS = new ArrayList<>();
        // 1. 아이디에 맞는 복용 목록 들고오기
        // 2. DTO로 변환해서 리스트로 반환
        return dosageDTOS;
    }

    /**
     * 복약 정보를 가져오는 함수(보여줄께 딱히 없어서 삭제 고려 중 가장 나중에 구현)
     * @param did 복약 아이디
     * @return 복약 정보 DTO
     * @author 박채빈
     *
     */
    public DosageDTO getDosageInfo(String did){
        // 1. 복용 목록에 맞는 아이디 정보 불러와서 반환
        return null;
    }

    /**
     * 복약 정보를 기록하는 함수
     * @param dosageDTO 복약 정보
     * @param uid 사용자 아이디
     * @return 저장의 결과
     * @author 박채빈
     *
     */
    public boolean addDosage(DosageDTO dosageDTO, String uid){
        // 1. 복용 정보와 아이디를 사용해 Entity로 변형
        // 2. 저장 후 결과 반환
        return false;
    }
}
