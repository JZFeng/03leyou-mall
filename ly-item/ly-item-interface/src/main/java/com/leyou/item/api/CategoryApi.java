package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    @RequestMapping("list")
    public List<Category> queryCategoryListByParentId( @RequestParam(name = "pid" , defaultValue = "0") Long pid );

    @GetMapping("bid/{bid}")
    public List<Category> queryByBrandId(@PathVariable("bid") Long bid);

    @GetMapping("names")
    public List<String> queryNameByIds(@RequestParam(name = "ids") List<Long> ids);

    @GetMapping
    public List<Category> queryCategoryByIds(@RequestParam(name = "ids") List<Long> ids) ;

    @GetMapping("all/level")
    public List<Category> queryAllByCid3( @RequestParam("cid3") Long cid3 );

}
