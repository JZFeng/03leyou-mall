/**
 * @Author jzfeng
 * @Date 5/23/21-11:33 AM
 */

package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
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
    private SearchService searchService;

    @RabbitListener(
            bindings = {
                @QueueBinding(
                    value = @Queue( value = "leyou.create.index.queue", durable = "true"),
                    exchange =  @Exchange(
                         value = "leyou.item.exchange",
                         ignoreDeclarationExceptions = "true",
                         type = ExchangeTypes.TOPIC
                    ),
                    key = {"item.insert", "item.update"}
                )
            }
    )

    public void listenCreate(Long spuId) {
        if(spuId == null || spuId < 0) {
            return;
        }

        this.searchService.createIndex(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue( value = "leyou.delete.index.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.delete"}
    ))

    public void listenDelete(Long spuId) {
        if(spuId == null || spuId < 0) {
            return;
        }

        this.searchService.deleteIndex(spuId);
    }

}
