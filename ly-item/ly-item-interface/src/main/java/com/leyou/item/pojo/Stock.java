/**
 * @Author jzfeng
 * @Date 4/28/21-9:51 AM
 */

package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_stock")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Stock implements Serializable  {
    @Id
    private Long skuId;
    private Integer seckillStock; //秒杀可用库存
    private Integer seckillTotal; //已秒杀数量
    private Integer stock;
}
