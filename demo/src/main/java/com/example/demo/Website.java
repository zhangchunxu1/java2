package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;
//使用 JPA 注解 @GeneratedValue 设置主键 id 为自动生成，并在 @GeneratedValue 中指定策略（例如 GenerationType.IDENTITY）：
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name = "website") // 指定数据库中的正确表名

public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自动生成 ID
    private Integer id;

    private String name;
    private String url;
    private int alexa;
    private String country;

    // Getter 和 Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAlexa() {
        return alexa;
    }

    public void setAlexa(int alexa) {
        this.alexa = alexa;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
