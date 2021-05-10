package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping("params")
    public String querySpecificationByCategoryId(@RequestParam("cid") Long cid);

}
