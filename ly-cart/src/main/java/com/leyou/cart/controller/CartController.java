/**
 * @Author jzfeng
 * @Date 5/31/21-6:22 PM
 */

package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart( @RequestBody Cart cart) {
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList() {
        List<Cart> carts = this.cartService.queryCartList();
        if ( CollectionUtils.isEmpty(carts) ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        try {
            this.cartService.updateNum(skuId, num);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("skuId")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId) {
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }

}
