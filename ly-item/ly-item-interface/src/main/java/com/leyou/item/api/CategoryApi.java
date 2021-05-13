package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    @GetMapping("names")
    public List<String> queryNameByIds(@RequestParam(name = "ids") List<Long> ids);

    @GetMapping("list")
    public List<Category> queryCategoryByIds(@RequestParam(name = "ids") List<Long> ids);
}
