/**
 * @Author jzfeng
 * @Date 5/24/21-3:53 PM
 */

package com.leyou.user.service;

import com.leyou.common.utils.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.leyou.user.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final String KEY_PREFIX = "user:code:phone:";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //生成随机码
    //保存到redis中
    //发送消息到ly-sms-service服务，发送短信
    public Boolean sendVerifyCode(String phone) {
        String code = NumberUtils.generateCode(6);

        try {
            // user:cde:phone:13611522870,894821
            this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);

            Map<String, String> msg = new HashMap<>();
            msg.put("phone", phone);
            msg.put("code", code);
            this.amqpTemplate.convertAndSend("leyou.sms.exchange", "sms.verify.code", msg);

            return  true;
        } catch (Exception e) {
            logger.error("发送短信失败. phone:{}, code:{},", phone, code);
            e.printStackTrace();
            return false;
        }
    }


    //校验验证码
    //生成盐，对密码加密，写入数据库，并删除redis中验证码;
    public Boolean register(User user, String code) {

        String key = KEY_PREFIX +  user.getPhone();
        String expected_code = redisTemplate.opsForValue().get(key);
        if( !expected_code.equals(code) ) {
            return false;
        }

        user.setId(null);
        user.setCreated(new Date());

        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        Boolean boo = (this.userMapper.insertSelective(user) == 1);

        if(boo) {
            try {
                this.redisTemplate.delete(key);
            } catch (Exception e) {
                logger.error("删除redis缓存失败,code{}", code, e);
            }
        }

        return boo;
    }


    public User queryByNameAndPassword(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if(user == null ) {
            return null;
        }

        if(user.getPassword().equals( CodecUtils.md5Hex(password, user.getSalt()) )) {
            return user;
        }

        return null;
    }
}
