package com.kivy0000.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kivy0000.beans.Product;


import java.util.List;


public interface ProductService extends IService<Product> {

    Integer insertSelective(Product product);

    List<Product> selectByText(String selectText);

    //选择性修改
    int updateSelective(Product product);
}
