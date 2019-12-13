package com.ryan.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ryan Li
 * @date 2019/12/13
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayAccountConfig {
    private String appId;

    private String privateKey;

    private String aliPayPublicKey;

    private String notifyUrl;

    private String returnUrl;

}
