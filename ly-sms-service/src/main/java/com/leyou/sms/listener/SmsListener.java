/**
 * @Author jzfeng
 * @Date 5/25/21-9:54 AM
 */

package com.leyou.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties properties;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "leyou.sms.queue", durable = "true"),
                    exchange = @Exchange(value = "leyou.sms.exchange", durable = "true", ignoreDeclarationExceptions = "true"),
                    key = {"sms.verify.code"}
            )
    })
    public void listenSms(Map<String, String> msg) {

        if(msg == null || msg.size() <= 0) {
            return;
        }

        String phone = msg.get("phone");
        String code = msg.get("code");

        if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            return;
        }

        SendSmsResponse sendSmsResponse = this.smsUtils.sendSms(phone, code, properties.getSignName(), properties.getVerifyCodeTemplate());
        //发送失败抛，直接异常
        throw new RuntimeException();
    }

}
