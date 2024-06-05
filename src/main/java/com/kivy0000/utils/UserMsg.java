package com.kivy0000.utils;

import java.io.Serializable;
import java.util.List;

import com.kivy0000.beans.User;

/**
 * @author kivy0000
 * @version 1.0
 */

public class UserMsg implements Serializable {
    private Integer code;
    private String vcode;
    private String text;
    private List<User> info;
    private Object expireUser;
    private Long expireTime;

    public UserMsg() {
    }

    public UserMsg(Integer code, String vcode, String text, Object expireUser, Long expireTime) {
        this.code = code;
        this.vcode = vcode;
        this.text = text;
        this.expireUser = expireUser;
        this.expireTime = expireTime;
    }

    public UserMsg(Integer code, String vcode, String text, List<User> info) {
        this.code = code;
        this.vcode = vcode;
        this.text = text;
        this.info = info;
    }

    public UserMsg(Integer code, String vcode, String text) {
        this.code = code;
        this.vcode = vcode;
        this.text = text;
    }

    public UserMsg(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<User> getInfo() {
        return info;
    }

    public void setInfo(List<User> info) {
        this.info = info;
    }

    public Object getExpireUser() {
        return expireUser;
    }

    public void setExpireUser(Object expireUser) {
        this.expireUser = expireUser;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "code=" + code +
                ", vcode='" + vcode + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public static UserMsg buildMsg(Integer code, String vcode, String text) {
        return new UserMsg(code, vcode, text);
    }

    public static UserMsg buildUserMsg(Integer code, String vcode,String text, Object user, Long along) {
        return new UserMsg(code,vcode,text,user,along);
    }

    public static UserMsg buildSimpleMsg(Integer code, String text) {
        return new UserMsg(code, text);
    }

    public static UserMsg buildAllMsg(Integer code, String vcode, String text, List<User> info) {
        return new UserMsg(code, vcode, text, info);
    }
}
