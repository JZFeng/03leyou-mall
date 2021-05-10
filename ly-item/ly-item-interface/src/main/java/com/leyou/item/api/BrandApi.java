package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("page")
    public PageResult<Brand> queryBrandByPage(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(name = "key", required = false) String key
    );

    @PostMapping
    public void saveBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandByCid(@PathVariable(name = "cid") Long cid);

    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable(name = "id") Long id);

}
