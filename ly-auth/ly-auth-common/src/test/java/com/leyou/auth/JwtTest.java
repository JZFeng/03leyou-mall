/**
 * @Author jzfeng
 * @Date 5/28/21-7:24 AM
 */

package com.leyou.auth;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    public static final String pubKeyPath = "/Users/jzfeng/Documents/git/03leyou-mall/ly-auth/rsa/rsa.pub";
    public static final String priKeyPath = "/Users/jzfeng/Documents/git/03leyou-mall/ly-auth/rsa/rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;



    @Test
    public void testRsaGenerateKeys() {
        try {
            RsaUtils.generateKey(pubKeyPath, priKeyPath, "123");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Before
    public void testGetRsa() {
        try {
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenerateToken() {
        UserInfo jason = UserInfo.builder().id(1L).username("Jason").build();
        String token = JwtUtils.generateToken(jason, privateKey, 5);
        System.out.println("token=" + token);
    }

    @Test
    public void testParseToken() {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJKYXNvbiIsImV4cCI6MTYyMjQwMjY5N30.Yc5RWoUJBL_Fco3P4ZymJIzx92G0o6_ucoZEziAoklKHol2L3jF95MVI2cOv59E_CSoRHznA3gbQQEqIOFRSwZI6QpWgw4-dAl-1MQ_Ye2zeWjQi9vqPaQpX6HO2hSNHwXZ85aAlzSi_GfXdhpIm3sK4qzhN5INhAtGSSc3eby4";
        UserInfo us = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println(us);
    }

}
