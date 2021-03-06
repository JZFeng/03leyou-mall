/**
 * @Author jzfeng
 * @Date 5/15/21-9:44 AM
 */

package com.leyou.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyGoodsWeb {
    public static void main(String[] args) {
        SpringApplication.run(LyGoodsWeb.class, args);
    }
}
