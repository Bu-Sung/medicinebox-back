package com.klp.medicinebox.controller;

import com.klp.medicinebox.SessionManager;
import com.klp.medicinebox.dto.DosageDTO;
import com.klp.medicinebox.dto.MixtureDTO;
import com.klp.medicinebox.service.DosageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DosageController {

    private final SessionManager sessionManager;
    private final DosageService dosageService;
    
    String user_id = "1111";
    
    
    // 자신의 복약 기록 
    @GetMapping("/dosage/getDosageList")
    public @ResponseBody List<DosageDTO> getDosageList(){
        return dosageService.getDosageList(user_id);
    }
    
    
    // 복약 정보 
    @GetMapping("/dosage/getDosageInfo")
    public @ResponseBody DosageDTO getDosageInfo(@RequestParam("did") Long did){
        return dosageService.getDosageInfo(did, user_id);
    }
    
    
    // 복약 기록
    @PostMapping("/dosage/addDosage")
    public @ResponseBody boolean addDosage(@RequestBody DosageDTO dosageDTO){
        return dosageService.addDosage(dosageDTO, user_id);
    }
      
    // 병용 금기 확인          
    @PostMapping("/dosage/getProhibitList")
    public @ResponseBody List<MixtureDTO> getProhibitList(@RequestBody DosageDTO dosageDTO){
        return dosageService.getProhibitList(dosageDTO, user_id);
    }        
    
    
    
}
