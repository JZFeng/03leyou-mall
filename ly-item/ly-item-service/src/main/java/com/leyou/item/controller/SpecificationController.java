/**
 * @Author jzfeng
 * @Date 4/24/21-12:18 PM
 */

package com.leyou.item.controller;

import com.leyou.item.pojo.Specification;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("groups/{id}")
    public ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id") Long id) {
        Specification spec = this.specificationService.queryByCategoryId(id);
        if(spec == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }
}
