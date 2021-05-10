/**
 * @Author jzfeng
 * @Date 5/10/21-5:04 AM
 */

package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
