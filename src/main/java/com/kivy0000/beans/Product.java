package com.kivy0000.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author kivy0000
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "product_shop")
@SuppressWarnings("unused")
public class Product implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "名称不能为空！")//字符串非空校验
    private String name;

    @NotEmpty(message = "番号不能为空！")//字符串非空校验
    private String productId;


    @Min(0)//生效于指定类型Big。。。
    private BigInteger Inventory;//库存

    @Min(0)//生效于指定类型Big。。。,与@NotNull冲突,因为@NotNull适合所有类型，而@Min不同
    private BigInteger sales;

    @NotEmpty(message = "分类不能为空！")//字符串非空校验
    private String parts;//分类

    //这里是作为json格式的处理，string格式的输出仍需要单独simpleDateFormat
    //注意,@Past默认使用的是jvm中的时间,注意时区设置
    //jsonFormat在接受和响应时都会对格式进行处理
    @Past(message = "日期不正确")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime productionTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime initTime;


    public Integer getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigInteger getInventory() {
        return Inventory;
    }

    public void setInventory(BigInteger inventory) {
        Inventory = inventory;
    }

    public BigInteger getSales() {
        return sales;
    }

    public void setSales(BigInteger sales) {
        this.sales = sales;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public LocalDateTime getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(LocalDateTime productionTime) {
        this.productionTime = productionTime;
    }

    public LocalDateTime getInitTime() {
        return initTime;
    }

    public void setInitTime(LocalDateTime initTime) {
        this.initTime = initTime;
    }
}
