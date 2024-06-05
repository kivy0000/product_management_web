package com.kivy0000.service.impl;

import com.kivy0000.beans.User;
import com.kivy0000.mapper.UserMapper;
import com.kivy0000.service.RegisterService;
import com.kivy0000.utils.Msg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author kivy0000
 * @version 1.0
 */
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Msg registerUser(User user, String vcode, HttpServletRequest hsr, String expireTime) {
        Logger.getGlobal().log(Level.INFO, user.toString());
        Logger.getGlobal().log(Level.INFO, vcode);
        //得到long数值时间,判断是否超时
        Date date = new Date();
        if (date.getTime() > Long.parseLong(expireTime) || !(Long.parseLong(expireTime) > 0)) {
            //超时
            return Msg.buildSimpleMsg(300, "warning");
        }

        //不超时，比对验证码
        Object icode = hsr.getSession().getAttribute("vcode");
        //错误
        if (!(vcode.equals(icode.toString())) || Objects.equals(icode.toString(), "")) {
            return Msg.buildSimpleMsg(600, "warning");
        }
        Integer addId = userMapper.addUser(user);
        //判断
        return addId > 0 ? Msg.buildSimpleMsg(200, "success") : Msg.buildSimpleMsg(400, "failed");

    }
}
