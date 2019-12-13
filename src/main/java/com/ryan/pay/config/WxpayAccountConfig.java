package com.ryan.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ryan Li
 * @date 2019/12/13
 */
@Component
@ConfigurationProperties(prefix = "wxpay")
@Data
public class WxpayAccountConfig {
    private String appId;

    private String mchId;

    private String machKey;

    private String notifyUrl;

    private String returnUrl;

}
