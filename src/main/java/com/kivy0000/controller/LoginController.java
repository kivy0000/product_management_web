package com.kivy0000.controller;

import com.kivy0000.beans.User;
import com.kivy0000.service.LoginService;
import com.kivy0000.service.RegisterService;
import com.kivy0000.service.VcodeService;
import com.kivy0000.utils.Msg;
import com.kivy0000.utils.UserMsg;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author kivy0000
 * @version 1.0
 * This class is used to provide registration login and related captCHA services
 * -
 * 这个类用于提供注册/登陆以及相关的服务
 */
@RestController
public class LoginController {


    @Resource
    private LoginService loginService;
    @Resource
    private RegisterService registerService;
    @Resource
    private VcodeService vcodeService;

    /**
     * 登录
     * service端中，已经将对象和过期时间作为json对象放入session
     */
    @PostMapping("/login")
    public UserMsg userLogin(@RequestBody User user, HttpServletRequest httpServletRequest) {
        return loginService.userLogin(user, httpServletRequest);
    }

    /**
     * 重置密码
     */
    @RequestMapping("/resetPassword/{vcode}/{expireTime}")
    public Msg resetPassword(@RequestBody User user, @PathVariable String expireTime,
                             @PathVariable String vcode, HttpServletRequest hsr) {
        return loginService.resetPassword(user, expireTime, vcode, hsr);
    }

    /**
     * 注册方法
     *
     * @param user 前端 注册数据
     * @return 200/400的成功，失败数据
     */
    @RequestMapping("/register/{vcode}/{expireTime}")
    public Msg registerUser(@RequestBody User user, @PathVariable String vcode, HttpServletRequest hsr, @PathVariable String expireTime) {
        return registerService.registerUser(user, vcode, hsr, expireTime);
    }
    /**
     * 注册验证码获取
     *
     * @return 200: 发送成功
     * 300: 账号/邮箱已注册，拒绝发送
     * 400: 邮箱错误，检查
     */
    @RequestMapping("/getVcode")
    public Msg sendRegisterVcode(@RequestBody User user, HttpServletRequest hsr) {
        return vcodeService.getRegisterVcode(user, hsr);
    }

    /**
     * 重置密码的验证码检查方法
     */
    @RequestMapping("/reSetVcode")
    public Msg logVcode(@RequestBody User user, HttpServletRequest hsr) {
        return vcodeService.getResetVcode(user, hsr);
    }
}
