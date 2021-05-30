/**
 * @Author jzfeng
 * @Date 5/30/21-12:24 PM
 */

package com.leyou.auth.service;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import com.leyou.auth.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String authentication(String username, String password) {
        User user = this.userClient.queryByNameAndPassword(username, password);
        if(user == null) {
            return null;
        }

        try {
            String token = JwtUtils.generateToken( new UserInfo(user.getId(), username), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
