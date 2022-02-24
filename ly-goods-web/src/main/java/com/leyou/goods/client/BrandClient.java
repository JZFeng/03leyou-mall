/**
 * @Author jzfeng
 * @Date 5/15/21-6:35 PM
 */

package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")

public interface BrandClient extends BrandApi {
}
