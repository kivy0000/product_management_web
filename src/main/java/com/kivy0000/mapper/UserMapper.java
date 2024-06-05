package com.kivy0000.mapper;

import com.kivy0000.beans.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author kivy0000
 * @version 1.0
 * The method is as the name suggests
 * -
 * 法如其名
 */
@Mapper
public interface UserMapper {

    Integer selectIdByUsername(User user);

    Integer selectIdByUsernameOrEmail(User user);

    Integer selectIdByUsernameAndEmail(User user);

    User selectUserByUsernameAndPassword(User user);

    Integer resetPassword(User user);

    Integer addUser(User user);
}
