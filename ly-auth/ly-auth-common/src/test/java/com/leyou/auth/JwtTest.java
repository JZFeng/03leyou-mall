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
    public static final String pubKeyPath = "/Users/jzfeng/Documents/git/leyou/ly-auth/ly-auth-common/rsa/rsa.pub";
    public static final String priKeyPath = "/Users/jzfeng/Documents/git/leyou/ly-auth/ly-auth-common/rsa/rsa.pri";

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

    @Before
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
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJKYXNvbiIsImV4cCI6MTYyMjIxOTk0N30.P5zqEuzp6_QIL9yiWKkRDdOX5YtelwhNXr7Il2AYtCwiZiN3vfy4i8zcX-SqwRQysWhbPqvCOAyNn_ACsmFry7drJhQ2l-l-bHDnjE1sGXsRYi-MUNY81P1H-__0pf0XD0acsyGSz87neLCLK4DT0kwC9_BgKgU7ir1w7AWqTzQ";
        UserInfo us = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println(us);
    }

}
