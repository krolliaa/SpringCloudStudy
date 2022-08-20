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
    private String brand;//品牌
    private String city;//城市
    private String starName;//星级
    private Integer minPrice;//最小价格
    private Integer maxPrice;//最大价格
}
