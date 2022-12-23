package com.example.monolithic_auth.controller;

import com.example.monolithic_auth.dto.UserRequestDto;
import com.example.monolithic_auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class HomeController {
    private final LoginService loginService;

    @Autowired
    public HomeController(LoginService loginService){
        this.loginService = loginService;

    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody final UserRequestDto params){
        Map<String, Object> response = new HashMap<>();

        if(loginService.login(params)){
            response.put("result","SUCCESS");
            response.put("user",params.getEmail());
        }else {
            response.put("result", "FAIL");
            response.put("reason", "일치하는 회원 정보가 없습니다.");
        }

        return response;
    }
}
