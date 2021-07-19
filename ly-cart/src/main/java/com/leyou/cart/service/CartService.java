/**
 * @Author jzfeng
 * @Date 5/31/21-6:22 PM
 */

package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "ly:cart:uid:";

    static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addCart(Cart cart) {
        UserInfo user = LoginInterceptor.getLoginUser();
//        UserInfo user = new UserInfo(1L, "jason");
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();

        Boolean boo = hashOps.hasKey(cart.getSkuId().toString());
        if (boo) {
            String json = hashOps.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            cart.setUserId(user.getId());
            Sku sku = this.goodsClient.querySkuById(skuId);
            cart.setTitle(sku.getTitle());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setOwnSpec(sku.getOwnSpec());
        }

        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));

    }

    public List<Cart> queryCartList() {
        List<Cart> result = new ArrayList<>();
        UserInfo user = LoginInterceptor.getLoginUser();
//        UserInfo user = new UserInfo(1L, "jason");
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);

        List<Object> carts = hashOps.values();
        if (CollectionUtils.isEmpty(carts)) {
            return result;
        } else {
            return carts.stream().map((o -> JsonUtils.parse(o.toString(), Cart.class)) ).collect(Collectors.toList());
        }
    }

    public void updateNum(Long skuId, Integer num) {
//        UserInfo user = LoginInterceptor.getLoginUser();
        UserInfo user = new UserInfo(1L, "jason");
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);
        Cart cart = JsonUtils.parse(hashOps.get(skuId.toString()).toString(), Cart.class);
        cart.setNum(num);
        hashOps.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(Long skuId) {
//        UserInfo user = LoginInterceptor.getLoginUser();
        UserInfo user = new UserInfo(1L, "jason");
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);

        hashOps.delete(skuId.toString());
    }

}
