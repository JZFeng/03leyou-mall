/**
 * @Author jzfeng
 * @Date 4/24/21-2:08 PM
 */

package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

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


}
