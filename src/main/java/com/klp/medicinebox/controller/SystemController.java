package com.klp.medicinebox.controller;

import com.klp.medicinebox.SessionManager;
import com.klp.medicinebox.dto.DrugDTO;
import com.klp.medicinebox.service.DrugService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SystemController {
    
    private final SessionManager sessionManager;
    
    @GetMapping("/")
    public String pageMain(Model model){
        sessionManager.setUserSession("test");
        return "index";
    }
}
