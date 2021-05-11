package com.leyou.item.api;

import com.leyou.item.pojo.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping("params")
    public String querySpecificationByCategoryId(@RequestParam("cid") Long cid);

    @GetMapping("{spuId}")
    public List<Param> queryParamsBySpuId(@PathVariable( name = "spuId") Long spuId);

}
