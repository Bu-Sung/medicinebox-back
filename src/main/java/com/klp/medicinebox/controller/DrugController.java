package com.klp.medicinebox.controller;

import com.klp.medicinebox.SessionManager;
import com.klp.medicinebox.dto.DrugDTO;
import com.klp.medicinebox.dto.ShapeDTO;
import com.klp.medicinebox.service.DrugService;
import com.klp.medicinebox.service.OCRService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RestController
@RequiredArgsConstructor
public class DrugController {

    private final SessionManager sessionManager;
    private final DrugService drugService;
    private final OCRService ocrService;
    
    String user_id = "1111";
    

//    // 제품 검색
//    @GetMapping("/drug/searchDrugList")
//    public @ResponseBody List<DrugDTO> searchDrugList(@RequestParam("type") int type, @RequestParam("search") String search) {
//        List<DrugDTO> result = drugService.searchDrugList(type, search);
//
//        if (result.isEmpty()) {  // searchDrugList 검색 결과가 없는 경우만 searchDrugList2 검색 함수 실행 
//            result = drugService.searchDrugList2(type, search);
//        }
//
//        return result;
//    }

    // 텍스트(제품명)로 검색 
    @GetMapping("/drug/textSearchList")
    public @ResponseBody List<DrugDTO> textSearchList(@RequestParam("search") String search) {
        List<DrugDTO> result = drugService.searchDrugList(2, search);

        if (result.isEmpty()) {  // searchDrugList 검색 결과가 없는 경우만 searchDrugList2 검색 함수 실행 
            result = drugService.searchDrugList2(2, search);
        }

        return result;
    }
    
    // 증상으로 검색
    @GetMapping("/drug/efcySearchList")
    public @ResponseBody List<DrugDTO> efcySearchList(@RequestParam("search") String search) {
        List<DrugDTO> result = drugService.searchDrugList(3, search);

        return result;
    }
    
    
    // 약품의 모양으로 검색 
    @PostMapping("/drug/getDrugFromShape")
    public @ResponseBody List<ShapeDTO> getDrugFromShape(@RequestBody ShapeDTO shapeDTO) {
        return drugService.getDrugFromShape(shapeDTO);
    }
    

    // 선택한 제품의 정보를 반환
    @GetMapping("/drug/getDrugInfo")
    public @ResponseBody DrugDTO getDrugInfo(@RequestParam("seq") String seq) {
        return drugService.getDrugInfo(seq);
    }

    
    // 선택한 제품을 자신의 제품 리스트에 추가
    @PostMapping("/drug/addMyDrug")
    public @ResponseBody boolean addMyDrug(@RequestBody DrugDTO drugDTO) {
        return drugService.addMyDrug(drugDTO,user_id);
    }
    
    
    // 자신이 등록한 제품 리스트 (filter = 등록순, 최신순) 
    @GetMapping("/drug/getMyDrugList")
    public @ResponseBody List<DrugDTO> getMyDrugList(@RequestParam("filter") String filter) {
        return drugService.getMyDrugList(user_id, filter);
    }
    
   
    // 선택한 보유 제품 정보
    @GetMapping("/drug/getMyDrugInfo")
    public @ResponseBody DrugDTO getMyDrugInfo(@RequestParam("pid") Long pid) {
        return drugService.getMyDrugInfo(pid);
    }
    
    
    // 선택한 보유 제품 정보 변경
    @PostMapping("/drug/updateMyDrug")
    public @ResponseBody boolean updateMyDrug(@RequestBody DrugDTO drugDTO) {
        return drugService.updateMyDrug(drugDTO);
    }
    
    
    // 자신의 보유 제품 제거
    @GetMapping("/drug/deleteMyDrug")
    public @ResponseBody boolean deleteMyDrug(@RequestParam("pid") Long pid) {
        return drugService.deleteMyDrug(pid);
    }
    

    //OCR로 제품 찾기
    @PostMapping("/drug/ocr")
    public @ResponseBody List<DrugDTO> getOCRSearch(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String rootPath = request.getSession().getServletContext().getRealPath("/");

        // 파일을 저장할 폴더를 지정합니다.
        File uploadDir = new File(rootPath + "/WEB-INF/productupload");

        // 지정된 폴더가 존재하지 않으면 생성합니다.
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 저장할 파일의 경로를 생성합니다.
        File fileToSave = new File(uploadDir, multipartFile.getOriginalFilename());

        // MultipartFile의 내용을 지정된 파일에 저장합니다.
        multipartFile.transferTo(fileToSave);

        List<DrugDTO> list =ocrService.getOCRResultDrug(fileToSave);

        return list;
    }

    // 수량 부족 약 리스트 
    @GetMapping("/drug/getMyDrugListCount")
    public @ResponseBody
    List<DrugDTO> getMyDrugListCount() {
        return drugService.getDrugListByType(user_id, 1);
    }

    // 유통기한 임박 약 리스트 
    @GetMapping("/drug/getMyDrugListExpirationDate")
    public @ResponseBody
    List<DrugDTO> getMyDrugListExpirationDate() {
        return drugService.getDrugListByType(user_id, 2);
    }

    // 유통기한 지난 약 리스트 
    @GetMapping("/drug/getMyDrugListPassExpirationDate")
    public @ResponseBody
    List<DrugDTO> getMyDrugListPassExpirationDate() {
        return drugService.getDrugListByType(user_id, 3);
    }

}
