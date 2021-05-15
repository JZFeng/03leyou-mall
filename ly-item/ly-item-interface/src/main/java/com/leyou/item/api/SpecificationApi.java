package com.leyou.item.api;

import com.leyou.item.pojo.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping("{cid}")
    public String querySpecificationByCid(@PathVariable(name = "cid") Long cid);

    @GetMapping("spu/{spuId}")
    public List<Param> querySpecsBySpuId(@PathVariable( name = "spuId") Long spuId);

    @GetMapping("cid/{cid}")
    public List<Param> querySpecsByCid(@PathVariable( name = "cid") Long cid);
}
