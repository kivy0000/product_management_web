package com.kivy0000.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kivy0000
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerTime;//注册时间
    private Integer limit;//权限划分
    private String context;//备注/备用信息sql
    private Double balance;// 余额


}
