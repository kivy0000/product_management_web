package com.kivy0000.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kivy0000.beans.User;
import com.kivy0000.mapper.UserMapper;
import com.kivy0000.service.LoginService;
import com.kivy0000.utils.Msg;
import com.kivy0000.utils.UserMsg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @author kivy0000
 * @version 1.0
 * 实现登录业务逻辑
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserMsg userLogin(User user, HttpServletRequest hsr) {
        //查询账号是否存在/合法
        Integer id = userMapper.selectIdByUsername(user);

        //查询账号密码是否正确
        User selectUser = userMapper.selectUserByUsernameAndPassword(user);


        //没有账号/不合法
        if (id == null) {
            return UserMsg.buildMsg(400, "error", "账号不存在，请注册");
        }
        //账号合法且密码正确
        if (id > 0 && selectUser!=null) {
            //返回数据，设置过期时间，毫秒
            long expireTime = new Date().getTime() + 60 * 60 * 1000;
            try {
                return UserMsg.buildUserMsg(200, "success", "登陆成功,欢迎您： ",new ObjectMapper().writeValueAsString(selectUser),expireTime);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        //账号合法但是密码错误
        if (id > 0) {
            return UserMsg.buildMsg(300, "warning", "密码错误");
        }
        //其他不合法
        return UserMsg.buildMsg(400, "error", "账号不存在，请注册");
    }

    @Override
    public Msg resetPassword(User user, String expireTime, String vcode, HttpServletRequest hsr) {
        //得到long数值时间,判断是否超时
        Date date = new Date();
        if (date.getTime() > Long.parseLong(expireTime) || !(Long.parseLong(expireTime) > 0)) {
            //超时
            return Msg.buildSimpleMsg(300, "warning");
        }

        //不超时，比对验证码
        Object icode = hsr.getSession().getAttribute("vcode");
        if (icode == null || !(vcode.equals(icode.toString())) || Objects.equals(icode.toString(), "")) {
            //错误
            return Msg.buildSimpleMsg(600, "warning");
        }
        Integer update = userMapper.resetPassword(user);
        //重置成功/失败
        return update > 0 ?
                Msg.buildMsg(200, "success", "success")
                : Msg.buildSimpleMsg(400, "failed");
    }
}
