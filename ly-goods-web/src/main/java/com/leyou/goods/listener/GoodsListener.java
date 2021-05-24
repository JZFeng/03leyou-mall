/**
 * @Author jzfeng
 * @Date 5/23/21-12:00 PM
 */

package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue( value = "leyou.create.web.queue", durable = "true", ignoreDeclarationExceptions = "true"),
                    exchange = @Exchange(value = "leyou.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
                    key = {"item.insert", "item.update"}
            )
    })
    public void listenCreate(Long spuId) {
        if( spuId == null || spuId < 0 ) {
            return;
        }

        this.goodsHtmlService.asyncSaveHtml(spuId);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue( value = "leyou.delete.web.queue", durable = "true", ignoreDeclarationExceptions = "true"),
                    exchange = @Exchange(value = "leyou.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
                    key = {"item.delete"}
            )
    })
    public void listenDelete(Long spuId) {
        if( spuId == null || spuId < 0 ) {
            return;
        }

        this.goodsHtmlService.asyncDeleteHtml(spuId);
    }

}
