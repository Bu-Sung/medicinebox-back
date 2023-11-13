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
}
