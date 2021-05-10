package com.leyou;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author jzfeng
 * @Date 5/8/21-5:59 PM
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients

public class LySearchService {
    public static void main(String[] args) {
        SpringApplication.run(LySearchService.class, args);
    }
}
