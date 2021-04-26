/**
 * @Author jzfeng
 * @Date 4/24/21-12:18 PM
 */

package com.leyou.item.controller;

import com.leyou.item.pojo.Specification;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping({"groups/{cid}", "{cid}", "" })
    public ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("cid") Long cid) {
        Specification spec = this.specificationService.queryByCategoryId(cid);
        if(spec == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }

    @GetMapping("params")
    public ResponseEntity<String> querySpecificationByCategoryId1(@RequestParam("cid") Long cid) {
        Specification spec = this.specificationService.queryByCategoryId(cid);
        if(spec == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }
}
