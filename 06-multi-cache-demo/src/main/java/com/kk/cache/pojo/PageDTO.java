package com.kk.cache.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private Long total; //展示商品总数
    private List<Item> list; //展示的商品
}