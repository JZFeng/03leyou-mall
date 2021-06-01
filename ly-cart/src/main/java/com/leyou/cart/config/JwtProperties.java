/**
 * @Author jzfeng
 * @Date 5/31/21-4:57 PM
 */

package com.leyou.cart.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "leyou.jwt")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class JwtProperties {
    private String pubKeyPath;
    private String cookieName;
    private PublicKey publicKey;
    public static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init() {
        this.publicKey = RsaUtils.getPublicKey(this.pubKeyPath);
    }

}
