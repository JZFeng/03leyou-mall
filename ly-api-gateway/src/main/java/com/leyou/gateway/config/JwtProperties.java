/**
 * @Author jzfeng
 * @Date 5/31/21-11:34 AM
 */

package com.leyou.gateway.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties
@Data @AllArgsConstructor @NoArgsConstructor @ToString @Builder
public class JwtProperties {
    private String pubKeyPath;
    private PublicKey publicKey;
    private String cookieName;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init() {
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥失败", e);
            e.printStackTrace();
        }
    }


}
