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

    @Deprecated
    @GetMapping({"groups/{cid}"})
    //这个group有什么用？
    public ResponseEntity<String> querySpecificationByGroup(@PathVariable("cid") Long cid) {
        Specification spec = this.specificationService.queryByCategoryId(cid);
        if(spec == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }

    @GetMapping("params")
    public ResponseEntity<String> querySpecificationByCategoryId(@RequestParam("cid") Long cid) {
        Specification spec = this.specificationService.queryByCategoryId(cid);
        if(spec == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }

    @GetMapping("{spuId}")
    public ResponseEntity<List<Param>> queryParamsBySpuId(@PathVariable( name = "spuId") Long spuId) {
        List<Param> params = this.specificationService.queryParamsBySpuId(spuId);
        if(CollectionUtils.isEmpty(params)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }


}
