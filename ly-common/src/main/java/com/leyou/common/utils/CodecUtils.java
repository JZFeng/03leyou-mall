/**
 * @Author jzfeng
 * @Date 5/25/21-11:52 PM
 */

package com.leyou.common.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class CodecUtils {

    public static String md5Hex(String data, String salt) {
        if(StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }

        return DigestUtils.md5Hex(salt + DigestUtils.md5Hex(data));
    }

    public static String shaHex(String data, String salt) {
        if(StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }

        return DigestUtils.sha512Hex(salt + DigestUtils.sha512Hex(data));
    }

    public static String generateSalt() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "");
    }

}
