package com.klp.medicinebox.controller;

import com.klp.medicinebox.SessionManager;
import com.klp.medicinebox.dto.DrugDTO;
import com.klp.medicinebox.dto.ShapeDTO;
import com.klp.medicinebox.service.DrugService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DrugController {

    private final SessionManager sessionManager;
    private final DrugService drugService;
    private final String userId = "test";
    
    @GetMapping("/search/{sid}/{search}")
    public @ResponseBody List<DrugDTO> getSearchDrugList(@PathVariable int sid, @PathVariable String search){
        return drugService.searchDrugList(sid, search);
    }
    
    @GetMapping("/drug/{pid}")
    public @ResponseBody DrugDTO getSearchDrugList(@PathVariable String pid){
        return drugService.getDrugInfo(pid);
    }
    
    @GetMapping("/mylist/{filter}")
    public @ResponseBody List<DrugDTO> getMyhDrugList(@PathVariable String filter){
        return drugService.getMyDrugList(userId, filter);
    }
    
    @PostMapping("/shape")
    public @ResponseBody List<ShapeDTO> getDrugFromShape(@RequestBody ShapeDTO shapeDTO){
        return drugService.getDrugFromShape(shapeDTO);
    }
            
    
}
