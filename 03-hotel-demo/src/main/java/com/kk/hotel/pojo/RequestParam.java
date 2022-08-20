package com.kk.hotel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestParam {
    private String key;//搜索关键字
    private Integer page;//当前页码 (page - 1) * size
    private Integer size;//显示条数
    private String sortBy;//排序字段
}
