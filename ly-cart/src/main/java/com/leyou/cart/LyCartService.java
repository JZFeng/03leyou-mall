/**
 * @Author jzfeng
 * @Date 5/31/21-2:12 PM
 */

package com.leyou.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients

public class LyCartService {
    public static void main(String[] args) {
        SpringApplication.run(LyCartService.class, args);
    }
}

