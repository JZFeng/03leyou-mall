/**
 * @Author jzfeng
 * @Date 5/1/21-9:19 PM
 */

package com.leyou.item.service;

import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    //根据SpuId查询sku和stock信息
    public List<Sku> queryBySpuId(Long id) {

        Sku record = new Sku();
        record.setSpuId(id);
        List<Sku> skus = this.skuMapper.select(record);
        for (Sku sku : skus) {
            Integer stock = this.stockMapper.selectByPrimaryKey(sku.getId()).getStock();
            sku.setStock(stock);
        }

        return skus;
    }
}
