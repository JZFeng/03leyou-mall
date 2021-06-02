package com.leyou.item.api;


import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {

    @GetMapping("/spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows,
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "saleable", required = false ) Boolean saleable
    );

    @GetMapping("spu/detail/{spuId}")
    public SpuDetails querySpuDetailsBySpuId(@PathVariable(name = "spuId") Long spuId);

    @GetMapping("spu/{spuId}")
    public Spu querySpuBySpuId(@PathVariable(name = "spuId") Long spuId);

    @GetMapping("sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam(name = "id") Long id);

    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id") Long id);

    @PostMapping("goods")
    public void saveGoods(@RequestBody SpuBo spu);

    @PutMapping("goods")
    public void updateGoods(@RequestBody SpuBo spu);


}
