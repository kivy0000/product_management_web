package com.kivy0000.service.impl;

import com.kivy0000.beans.User;
import com.kivy0000.mapper.UserMapper;
import com.kivy0000.service.VcodeService;
import com.kivy0000.utils.MailUtil;
import com.kivy0000.utils.Msg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author kivy0000
 * @version 1.0
 */
@Service
@Transactional
public class VcodeServiceImpl implements VcodeService {


    @Resource
    private UserMapper userMapper;

    @Override//获取注册验证码
    @SuppressWarnings("all")
    public Msg getRegisterVcode(User user, HttpServletRequest httpServletRequest) {
        //查询是否已经注册
        Integer integer = userMapper.selectIdByUsernameOrEmail(user);
        if (integer == null || integer == 0) {
            //未注册
            //使用默认发件人发送验证码
            String vcode = MailUtil.sendVcode(user.getEmail());
            Logger.getGlobal().log(Level.INFO,
                    vcode != null && !Objects.equals(vcode, "failed") ?
                            "注册验证码为" + vcode :
                            "获取验证码失败，请检查邮箱");

            /**保存到session中，注意:在验证时要注意允许request的cookie传递*/
            httpServletRequest.getSession().setAttribute("vcode", vcode);
            return vcode != null && !Objects.equals(vcode, "failed") ?
                    Msg.buildSimpleMsg(200, "success")
                    : Msg.buildSimpleMsg(400, "failed");
        }
        //已注册
        return Msg.buildSimpleMsg(300, "failed");
    }

    @Override//获取重置验证码
    public Msg getResetVcode(User user, HttpServletRequest httpServletRequest) {
        //查询是否为合法用户
        Integer isUser = userMapper.selectIdByUsernameAndEmail(user);
        //同时判断是否是账号/邮箱填错了,有这个用户/邮箱
        Integer haveUser = userMapper.selectIdByUsernameOrEmail(user);

        //存在账号，但是账号/邮箱不匹配
        if (!(isUser > 0) && haveUser > 0) {
            return Msg.buildSimpleMsg(500, "failed");
        }
        //填写正确
        if (isUser > 0) {
            //使用默认发件人发送验证码
            String vcode = MailUtil.sendVcode(user.getEmail());
            Logger.getGlobal().log(Level.INFO,
                    vcode != null && !Objects.equals(vcode, "failed") ?
                            "重置验证码为" + vcode :
                            "获取验证码失败，请检查邮箱");
            //保存验证码到域
            httpServletRequest.getSession().setAttribute("vcode", vcode);
            return vcode != null && !Objects.equals(vcode, "failed") ?
                    Msg.buildMsg(200, vcode, "success")
                    : Msg.buildSimpleMsg(400, "failed");
        }
        //非法用户,未注册
        return Msg.buildSimpleMsg(300, "failed");
    }
}
