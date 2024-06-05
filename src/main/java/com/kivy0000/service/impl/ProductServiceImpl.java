package com.kivy0000.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kivy0000.OException.MySqlException;
import com.kivy0000.beans.Product;
import com.kivy0000.mapper.ProductMapper;
import com.kivy0000.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author kivy0000
 * @version 1.0
 */
@Service
@Transactional
public class ProductServiceImpl
        extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    @Resource
    private ProductMapper productMapper;

    /**
     * 选择性添加产品
     *
     * @return 成功 > 0，否则 < 0
     */
    @Override
    public Integer insertSelective(Product product) {
        try {
            productMapper.insertSelective(product);
        } catch (Exception e) {
            Logger.getLogger("com.kfhstu.service.insertSelective.38").log(Level.SEVERE, "sql出现异常，请检查" + e.getMessage());
            throw new MySqlException();
        }
        return Optional.ofNullable(product.getId()).orElse(-1);
    }

    /**
     * 根据name/pid查找
     * queryWrapper 条件
     */
    @Override
    public List<Product> selectByText( String selectText) {
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<Product>()
                .select("id", "name", "product_id", "Inventory", "sales", "parts","production_time","init_Time")
                .like("name", selectText)
                .or()
                .like("product_id", selectText);
        return list(productQueryWrapper);
    }

    @Override
    public int updateSelective(Product product) {
        return productMapper.updateSelective(product);
    }


}
