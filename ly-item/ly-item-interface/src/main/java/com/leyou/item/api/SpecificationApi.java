package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping({"groups/{cid}", "{cid}", "" })
    public String querySpecificationByCategoryId(@PathVariable("cid") Long cid);

    @GetMapping("params")
    public String querySpecificationByCategoryId1(@RequestParam("cid") Long cid);
}
