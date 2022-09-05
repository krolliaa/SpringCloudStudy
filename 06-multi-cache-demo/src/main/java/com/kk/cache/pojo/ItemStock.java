package com.kk.cache.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStock {
    @TableId(type = IdType.INPUT, value = "item_id")
    private Long id; //商品id
    private Integer stock; //商品库存
    private Integer sold; //商品销量
}
