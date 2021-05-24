/**
 * @Author jzfeng
 * @Date 5/15/21-8:51 PM
 */

package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
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

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("{id}.html")
    public ModelAndView toItemPage( @PathVariable(name = "id") Long id) {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> objectMap = this.goodsService.loadModel(id);
        mv.addAllObjects(objectMap);
        if(objectMap != null && objectMap.size() > 0) {
            goodsHtmlService.asyncSaveHtml(id, objectMap); //异步保存
            mv.setViewName("item");
        } else {
            mv.setViewName("error");
        }

        return mv;
    }


}
