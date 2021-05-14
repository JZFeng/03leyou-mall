/**
 * @Author jzfeng
 * @Date 4/24/21-12:18 PM
 */

package com.leyou.item.controller;

import com.leyou.item.pojo.Param;
import com.leyou.item.pojo.Specification;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("{cid}")
    public ResponseEntity<String> querySpecificationByCid(@PathVariable(name = "cid") Long cid) {
        Specification spec = this.specificationService.queryByCategoryId(cid);
        if(spec == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }

    // Specs规格参数，指的是Map<String,String> = {"分辨率":"其他","品牌":"null","CPU核数":"八核","机身颜色":["黑色","蓝色"]}
    // Param对象：{id=4, groupId=2, k=机身颜色, searchable=false, global=false, numerical=false, v=null, unit=null, options=[金, 粉, 灰, 银]}
    // Specifications 是那个包含所有ParamGroupJson字符串
    @GetMapping("spu/{spuId}")
    public ResponseEntity<List<Param>> querySpecsBySpuId(@PathVariable( name = "spuId") Long spuId) {
        List<Param> params = this.specificationService.querySpecsBySpuId(spuId);
        if(CollectionUtils.isEmpty(params)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Param>> querySpecsByCid(@PathVariable( name = "cid") Long cid) {
        List<Param> params = this.specificationService.querySpecsByCid(cid);
        if(CollectionUtils.isEmpty(params)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }
}
