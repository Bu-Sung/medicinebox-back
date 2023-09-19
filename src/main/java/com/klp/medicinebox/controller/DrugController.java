package com.klp.medicinebox.controller;

import com.klp.medicinebox.SessionManager;
import com.klp.medicinebox.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DrugController {

    private final SessionManager sessionManager;
    private final DrugService drugService;

}
