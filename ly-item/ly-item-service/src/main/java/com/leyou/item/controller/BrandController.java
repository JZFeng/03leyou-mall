/**
 * @Author jzfeng
 * @Date 4/17/21-2:45 PM
 */

package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(name = "key", required = false) String key
    ) {
        PageResult<Brand> result =  this.brandService.queryBrandByPageAndSort(page, rows, sortBy, desc, key);
        if(result == null || result.getItems().size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable(name = "cid") Long cid) {
        List<Brand> brands =  this.brandService.queryBrandByCid(cid);
        if(brands == null || brands.size() < 1) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable(name = "id") Long id ) {
        Brand brand = this.brandService.queryBrandById(id);
        if(brand == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam(name = "ids") List<Long> ids) {
        List<Brand> brands = this.brandService.queryBrandByIds(ids);
        if(CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }
}
