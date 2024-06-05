package com.kivy0000.OException;

/**
 * @author kivy0000
 * @version 1.0
 * 用于替代默认的sqlException，使得日志清晰
 */
public class MySqlException extends RuntimeException {

    @Override
    public String getMessage() {
        return "出现了sql异常";
    }


}
