/**
 * @Author jzfeng
 * @Date 5/25/21-10:53 AM
 */

package com.leyou.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyUserService.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        this.stringRedisTemplate.opsForValue().set("key1","value1");
        System.out.println(this.stringRedisTemplate.opsForValue().get("key1"));
    }
}
