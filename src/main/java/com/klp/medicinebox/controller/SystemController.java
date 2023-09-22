package com.klp.medicinebox.controller;

import com.klp.medicinebox.dto.DrugDTO;
import com.klp.medicinebox.service.DrugService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SystemController {
    
    private final DrugService drugService;
    
    @GetMapping("/")
    public String pageMain(Model model){
        model.addAttribute("list",drugService.searchDrugList(2, "타이레놀"));
        return "index";
    }
}
