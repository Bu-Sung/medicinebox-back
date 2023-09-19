package com.klp.medicinebox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    @Autowired
    private HttpSession session;

    private final String userSession = "user";

    public void setUserSession(String uid){ // 사용자 아이디 세션 저장
        session.setAttribute(userSession, uid);
        session.setMaxInactiveInterval(-1);
    }

    public String getUserId(){ // 사용자 아이디 반환
        return (String) session.getAttribute(userSession);
    }
}
