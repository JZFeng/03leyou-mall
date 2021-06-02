package com.leyou.cart.service;


import com.leyou.cart.LyCartService;
import com.leyou.cart.pojo.Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyCartService.class)
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    public void addCartTest() {
        Cart cart1 = Cart.builder().userId(1L).skuId(27359021649L).num(2).build();
        Cart cart2 = Cart.builder().userId(1L).skuId(27359021518L).num(1).build();
        this.cartService.addCart(cart1);
        this.cartService.addCart(cart2);
    }

    @Test
    public void queryCartListTest() {
        List<Cart> carts = this.cartService.queryCartList();
        carts.forEach( cart -> {System.out.println(cart);});
    }

    @Test
    public void updateNumTest() {
        this.cartService.updateNum(27359021518L, 5);
    }

    @Test
    public void deleteCartTest() {
        this.cartService.deleteCart(27359021518L);
    }
}