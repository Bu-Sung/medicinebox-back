package com.klp.medicinebox.controller;

import com.klp.medicinebox.SessionManager;
import com.klp.medicinebox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final SessionManager sessionManager;
    private final UserService userService;

}
