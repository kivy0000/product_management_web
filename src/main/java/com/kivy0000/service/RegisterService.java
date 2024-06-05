package com.kivy0000.service;


import com.kivy0000.beans.User;
import com.kivy0000.utils.Msg;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kivy0000
 * @version 1.0
 * 注册相关业务逻辑
 */
public interface RegisterService {

    //注册方法
    public Msg registerUser(User user, String vcode, HttpServletRequest hsr, String expireTime);
}
