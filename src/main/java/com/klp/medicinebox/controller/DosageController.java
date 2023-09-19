package com.klp.medicinebox.controller;

import com.klp.medicinebox.SessionManager;
import com.klp.medicinebox.service.DosageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DosageController {

    private final SessionManager sessionManager;
    private final DosageService dosageService;
}
