/**
 * @Author jzfeng
 * @Date 4/24/21-2:08 PM
 */

package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetails;
import com.leyou.item.service.GoodsService;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SpuDetailsService spuDetailsService;

    @Autowired
    private SkuService skuService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
        @RequestParam(name = "page",defaultValue = "1") Integer page,
        @RequestParam(name = "rows", defaultValue = "5") Integer rows,
        @RequestParam(name = "key", required = false) String key,
        @RequestParam(name = "saleable", required = false ) Boolean saleable
    ) {
        PageResult<SpuBo> result =  this.goodsService.querySpuByPageAndSort(page, rows, key, saleable);
        if(result == null || result.getItems().size() == 0 ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam(name = "id") Long id) {
        List<Sku> skus = this.skuService.queryBySpuId(id);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(skus);
    }


    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spu) {
        try {
            this.goodsService.save(spu);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spu) {
        try {
            this.goodsService.updateGoods(spu);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetails> querySpuDetailsBySpuId(@PathVariable(name = "spuId") Long spuId) {
        SpuDetails spuDetails = this.spuDetailsService.queryBySpuId(spuId);
        if(spuDetails == null ) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(spuDetails);
    }

}
