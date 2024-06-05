package com.kivy0000.controller;


import com.kivy0000.utils.MailUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author kivy0000
 * @version 1.0
 * THIS CLASS PROMPTS FOR INITIALIZATION
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String index() {
        String defaultPassword = MailUtil.getDefaultPassword();
        String defaultSender = MailUtil.getDefaultSender();
        Logger.getGlobal().log(Level.INFO, defaultSender + defaultPassword);
        return defaultSender != null && defaultPassword != null && !defaultSender.equals("") && !defaultPassword.equals("")
                ? "初始化完成，你的验证码默认发件人为" + defaultSender + "，\n默认发件人授权码" + defaultPassword
                : "初始化失败，请检查application.yml文件";
    }

}
