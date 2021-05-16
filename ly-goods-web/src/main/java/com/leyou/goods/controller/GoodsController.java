/**
 * @Author jzfeng
 * @Date 5/15/21-8:51 PM
 */

package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("itm")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("{id}.html")
    public ModelAndView toItemPage( @PathVariable(name = "id") Long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("item");
        Map<String, Object> objectMap = this.goodsService.loadModel(id);
        mv.addAllObjects(objectMap);

        return mv;
    }


}
