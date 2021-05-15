/**
 * @Author jzfeng
 * @Date 4/16/21-9:58 PM
 */

package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByParentId( @RequestParam(name = "pid" , defaultValue = "0") Long pid ) {

        try {
            if(pid == null || pid.longValue() < 0) {
                return ResponseEntity.badRequest().build();
            }

            List<Category> categoryList = this.categoryService.queryCategoryListByParentId(pid);
            if(CollectionUtils.isEmpty(categoryList)) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> categories = this.categoryService.queryByBrandId(bid);
        if(categories == null || categories.size() < 1 ) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categories);
    }

    @GetMapping("names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam(name = "ids") List<Long> ids) {
        List<String> names = this.categoryService.queryNameByIds(ids);
        if(CollectionUtils.isEmpty(names)) {
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam(name = "ids") List<Long> ids) {
        List<Category> categories = this.categoryService.queryCategoryByIds(ids);
        if(CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categories);
    }

    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3( @RequestParam("cid3") Long cid3 ) {
        List<Category> categories = this.categoryService.queryAllByCid3(cid3);
        if(CollectionUtils.isEmpty(categories)) {
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

}
