/**
 * @Author jzfeng
 * @Date 5/15/21-8:17 PM
 */

package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public Map<String, Object> loadModel(Long spuId) {

        try {
            Spu spu = this.goodsClient.querySpuBySpuId(spuId);
            SpuDetails spuDetails = this.goodsClient.querySpuDetailsBySpuId(spuId);
            List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);
            Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
            List<Category> categories = this.categoryClient.queryAllByCid3(spu.getCid3());
            List<ParamGroup> paramGroups = this.specificationClient.querySpecGroups(spu.getCid3());
            //查询所有特有规格参数
            List<Param> specialParams = this.specificationClient.querySpecParam(null, spu.getCid3(), null, false);
            Map<Long, String> paramMap = new HashMap<>();
            for (Param param : specialParams) {
                paramMap.put(param.getId(), param.getK());
            }

            Map<String, Object> map = new HashMap<>();
            map.put("spu", spu);
            map.put("spuDetails", spuDetails);
            map.put("skus", skus);
            map.put("brand", brand);
            map.put("categories", categories);
            map.put("params", paramMap);
            map.put("groups", paramGroups);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }


}
