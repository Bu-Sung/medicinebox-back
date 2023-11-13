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
    

//    // ��ǰ �˻�
//    @GetMapping("/drug/searchDrugList")
//    public @ResponseBody List<DrugDTO> searchDrugList(@RequestParam("type") int type, @RequestParam("search") String search) {
//        List<DrugDTO> result = drugService.searchDrugList(type, search);
//
//        if (result.isEmpty()) {  // searchDrugList �˻� ����� ���� ��츸 searchDrugList2 �˻� �Լ� ���� 
//            result = drugService.searchDrugList2(type, search);
//        }
//
//        return result;
//    }

    // �ؽ�Ʈ(��ǰ��)�� �˻� 
    @GetMapping("/drug/textSearchList")
    public @ResponseBody List<DrugDTO> textSearchList(@RequestParam("search") String search) {
        List<DrugDTO> result = drugService.searchDrugList(2, search);

        if (result.isEmpty()) {  // searchDrugList �˻� ����� ���� ��츸 searchDrugList2 �˻� �Լ� ���� 
            result = drugService.searchDrugList2(2, search);
        }

        return result;
    }
    
    // �������� �˻�
    @GetMapping("/drug/efcySearchList")
    public @ResponseBody List<DrugDTO> efcySearchList(@RequestParam("search") String search) {
        List<DrugDTO> result = drugService.searchDrugList(3, search);

        return result;
    }
    
    
    // ��ǰ�� ������� �˻� 
    @PostMapping("/drug/getDrugFromShape")
    public @ResponseBody List<ShapeDTO> getDrugFromShape(@RequestBody ShapeDTO shapeDTO) {
        return drugService.getDrugFromShape(shapeDTO);
    }
    

    // ������ ��ǰ�� ������ ��ȯ
    @GetMapping("/drug/getDrugInfo")
    public @ResponseBody DrugDTO getDrugInfo(@RequestParam("seq") String seq) {
        return drugService.getDrugInfo(seq);
    }

    
    // ������ ��ǰ�� �ڽ��� ��ǰ ����Ʈ�� �߰�
    @PostMapping("/drug/addMyDrug")
    public @ResponseBody boolean addMyDrug(@RequestBody DrugDTO drugDTO) {
        return drugService.addMyDrug(drugDTO,user_id);
    }
    
    
    // �ڽ��� ����� ��ǰ ����Ʈ (filter = ��ϼ�, �ֽż�) 
    @GetMapping("/drug/getMyDrugList")
    public @ResponseBody List<DrugDTO> getMyDrugList(@RequestParam("filter") String filter) {
        return drugService.getMyDrugList(user_id, filter);
    }
    
   
    // ������ ���� ��ǰ ����
    @GetMapping("/drug/getMyDrugInfo")
    public @ResponseBody DrugDTO getMyDrugInfo(@RequestParam("pid") Long pid) {
        return drugService.getMyDrugInfo(pid);
    }
    
    
    // ������ ���� ��ǰ ���� ����
    @PostMapping("/drug/updateMyDrug")
    public @ResponseBody boolean updateMyDrug(@RequestBody DrugDTO drugDTO) {
        return drugService.updateMyDrug(drugDTO);
    }
    
    
    // �ڽ��� ���� ��ǰ ����
    @GetMapping("/drug/deleteMyDrug")
    public @ResponseBody boolean deleteMyDrug(@RequestParam("pid") Long pid) {
        return drugService.deleteMyDrug(pid);
    }
    

    //OCR�� ��ǰ ã��
    @PostMapping("/drug/ocr")
    public @ResponseBody List<DrugDTO> getOCRSearch(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String rootPath = request.getSession().getServletContext().getRealPath("/");

        // ������ ������ ������ �����մϴ�.
        File uploadDir = new File(rootPath + "/WEB-INF/productupload");

        // ������ ������ �������� ������ �����մϴ�.
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // ������ ������ ��θ� �����մϴ�.
        File fileToSave = new File(uploadDir, multipartFile.getOriginalFilename());

        // MultipartFile�� ������ ������ ���Ͽ� �����մϴ�.
        multipartFile.transferTo(fileToSave);

        List<DrugDTO> list =ocrService.getOCRResultDrug(fileToSave);

        return list;
    }

    // ���� ���� �� ����Ʈ 
    @GetMapping("/drug/getMyDrugListCount")
    public @ResponseBody
    List<DrugDTO> getMyDrugListCount() {
        return drugService.getDrugListByType(user_id, 1);
    }

    // ������� �ӹ� �� ����Ʈ 
    @GetMapping("/drug/getMyDrugListExpirationDate")
    public @ResponseBody
    List<DrugDTO> getMyDrugListExpirationDate() {
        return drugService.getDrugListByType(user_id, 2);
    }

    // ������� ���� �� ����Ʈ 
    @GetMapping("/drug/getMyDrugListPassExpirationDate")
    public @ResponseBody
    List<DrugDTO> getMyDrugListPassExpirationDate() {
        return drugService.getDrugListByType(user_id, 3);
    }

}
