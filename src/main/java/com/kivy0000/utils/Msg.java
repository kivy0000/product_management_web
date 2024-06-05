package com.kivy0000.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @author kivy0000
 * @version 1.0
 */

public class Msg implements Serializable {
    private Integer code;
    private String vcode;
    private String text;
    private List<Object> info;

    public Msg() {
    }

    public Msg(Integer code, String vcode, String text, List<Object> info) {
        this.code = code;
        this.vcode = vcode;
        this.text = text;
        this.info = info;
    }

    public Msg(Integer code, String vcode, String text) {
        this.code = code;
        this.vcode = vcode;
        this.text = text;
    }

    public Msg(Integer code, String text) {
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

    public String getTest() {
        return text;
    }

    public void setTest(String text) {
        this.text = text;
    }

    public List<Object> getInfo() {
        return info;
    }

    public void setInfo(List<Object> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "code=" + code +
                ", vcode='" + vcode + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public static Msg buildMsg(Integer code, String vcode, String text) {
        return new Msg(code, vcode, text);
    }

    public static Msg buildSimpleMsg(Integer code, String text) {
        return new Msg(code, text);
    }

    public static Msg buildAllMsg(Integer code, String vcode, String text, List<Object> info) {
        return new Msg(code, vcode, text, info);
    }
}
