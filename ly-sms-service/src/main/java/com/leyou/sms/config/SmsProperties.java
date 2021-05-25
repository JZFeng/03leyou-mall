/**
 * @Author jzfeng
 * @Date 5/25/21-8:55 AM
 */

package com.leyou.sms.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@ConfigurationProperties(prefix = "leyou.sms")
public class SmsProperties {

    String accessKey;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;


}
