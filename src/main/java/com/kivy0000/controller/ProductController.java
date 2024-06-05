package com.kivy0000.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kivy0000.beans.Product;
import com.kivy0000.service.ProductService;
import com.kivy0000.utils.Result;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @author kivy0000
 * @version 1.0
 */
@RestController
@SuppressWarnings("rawtypes")//抑制原始使用警告
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 测试
     */
    @RequestMapping("/")
    public Result<List<Product>> index() {
        return Result.success(productService.list());
    }

    /**
     * 查询所有设备
     */
    @GetMapping("getAll")
    public Result getAllProduct() {
        List<Product> pList = productService.list();
        return (pList == null || pList.size() <= 0) ? Result.warning() : Result.success(pList);
    }

    /**
     * 分页查询所有设备
     *
     * @param pageNum  页码/起始页码
     * @param pageSize 每页条数
     */
    @GetMapping("/getAllByPage")
    public Result getAllProductByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Product> productPage = new Page<>(pageNum, pageSize);
        Page<Product> page = productService.page(productPage);
        //结构变为Result.data.records
        return (page == null || page.getSize() <= 0) ? Result.warning() : Result.success(page);

    }

    /**
     * 选择性添加设备，post
     * -@Validated 验证bean
     */
    @PostMapping("/addProduct")
    public Result addProduct(@Valid @RequestBody Product product, Errors errors) {

        //创建接收错误信息的集合
        HashMap<String, Object> errorMap = new HashMap<>();
        //遍历接收错误
        List<FieldError> fieldErrors = errors.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        //判断是否有错误
        if (errorMap.isEmpty()) {
            Integer id = productService.insertSelective(product);
            return (id <= 0) ? Result.warning() : Result.success();
        } else {
            //出现校验错误会封装errors
            return Result.error("400", "error", errorMap);
        }


    }

    /**
     * 根据ID删除项目
     */
    @DeleteMapping("/deleteProduct")
    public Result deleteProduct(@RequestBody Product product) {
        return productService.removeById(product.getId()) ? Result.success() : Result.warning();
    }

    /**
     * 根据批量删除项目
     */
    @DeleteMapping("/deleteMoreProduct")
    public Result deleteMoreProduct(@RequestBody List<Integer> ids) {
        return productService.removeByIds(ids) ? Result.success() : Result.warning();
    }

    /**
     * 根据关键字查找项目
     *
     * @param keyWord 关键字
     */
    @GetMapping("/selectByText/{keyWord}")
    public Result selectByText(@PathVariable String keyWord) {
        List<Product> products = productService.selectByText(keyWord);
        return products.size() > 0 ? Result.success(products) : Result.warning();
    }


    /**
     * 分页根据关键字查找项目
     *
     * @param keyWord 关键字
     */
    @GetMapping("/selectByTextAndPage/{keyWord}")
    public Result selectByTextAndPage(@PathVariable String keyWord,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Product> productPage = new Page<>(pageNum, pageSize);
/*   不使用lambdaQueryWrapper<Product> productQueryWrapper = new QueryWrapper<Product>()
//                .select("id", "name", "product_id", "Inventory", "sales", "parts", "production_time", "init_Time")
//                .like("name", keyWord)
//                .or()
//                .like("product_id", keyWord);*/

        LambdaQueryWrapper<Product> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Product::getName, keyWord).or().like(Product::getProductId, keyWord);
        Page<Product> page = productService.page(productPage, lambdaQueryWrapper);
        return page.getRecords().size() > 0 ? Result.success(page) : Result.warning();
    }

    /**
     * 选择性修改，如果没有修改，返回warning无修改
     */
    @PutMapping("/editProduct")
    public Result editProduct(@RequestBody Product product) {
        try {
            return productService.updateSelective(product) > 0 ? Result.success() : Result.warning();
        } catch (Exception e) {
            return Result.error("401", "error", e.getClass());
        }
    }

    //TODO 从分页开始看，注意lambada

}
