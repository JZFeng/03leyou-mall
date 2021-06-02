/**
 * @Author jzfeng
 * @Date 5/25/21-10:53 AM
 */

package com.leyou.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyUserService.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis1() {
        this.stringRedisTemplate.opsForValue().set("key1","value1");
        System.out.println(this.stringRedisTemplate.opsForValue().get("key1"));
    }

    @Test
    public void testRedis2() {
        this.stringRedisTemplate.opsForValue().set("key2","value2",5, TimeUnit.SECONDS);
    }

    @Test
    public void testHash() {
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps("user");
        hashOps.put("name","jack");
        hashOps.put("age","21");
        hashOps.expire(14, TimeUnit.DAYS); //保留14天，等checkout之后就删除
        //
        System.out.println(hashOps.get("name"));

        Map<Object, Object> entries = hashOps.entries();
        entries.entrySet().forEach( e -> {
            System.out.println(e.getKey() + ":" + e.getValue());
        });

    }
}
