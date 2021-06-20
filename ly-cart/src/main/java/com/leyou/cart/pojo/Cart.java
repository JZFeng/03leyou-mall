/**
 * @Author jzfeng
 * @Date 5/31/21-6:42 PM
 */

package com.leyou.cart.pojo;

import lombok.*;

import java.io.Serializable;

@Data @AllArgsConstructor @Builder @ToString @NoArgsConstructor
public class Cart implements Serializable {
    private Long userId;
    private Long skuId;
    private String title;
    private String image;
    private Long price;
    private Integer num;
    private String ownSpec;

}
