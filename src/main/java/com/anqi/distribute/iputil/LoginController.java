package com.anqi.distribute.iputil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("user")
public class LoginController {

    @GetMapping("login")
    public String login() {
        //登录逻辑
        return "success";
    }
}
