package com.klp.medicinebox.controller;

import com.klp.medicinebox.dto.DrugDTO;
import com.klp.medicinebox.dto.RequestJson;
import com.klp.medicinebox.service.AiDrug;
import com.klp.medicinebox.service.DrugService;
import com.klp.medicinebox.service.OCRService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class SystemController {

    private final DrugService drugService;
    private final OCRService ocrService;
    private final AiDrug aiService;

    @GetMapping("/")
    public String pageMain(Model model) {
        drugService.addMedicineShape();
        //model.addAttribute("list",drugService.searchDrugList(2, "타이레놀"));
        return "index";
    }

    @PostMapping("/ocr")
    public @ResponseBody
      //  void getOCR() throws IOException {
    void getOCR(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
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

        List<DrugDTO> list = ocrService.getOCRResultDrug(fileToSave);
        for(DrugDTO d : list){
            System.out.println(d.getName());
        }
    }
    
    @PostMapping(value = "/v1.0/recognition")
    public @ResponseBody List<DrugDTO> recognition(@ModelAttribute RequestJson request) {
        List<String> names = aiService.getAiDrugName(request);
        return aiService.getAiDrugInfo(names);
    }

}
