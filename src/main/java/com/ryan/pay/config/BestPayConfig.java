package com.ryan.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Ryan Li
 * @date 2019/12/12
 */
@Component
public class BestPayConfig {

    @Autowired
    WxpayAccountConfig wxpayAccountConfig;

    @Autowired
    AlipayAccountConfig alipayAccountConfig;

    @Bean
    public BestPayService bestPayService() {
        //微信支付配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxpayAccountConfig.getAppId());          //公众号Id
        //支付商户资料
        wxPayConfig.setMchId(wxpayAccountConfig.getMchId());
        wxPayConfig.setMchKey(wxpayAccountConfig.getMachKey());
        wxPayConfig.setNotifyUrl(wxpayAccountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxpayAccountConfig.getReturnUrl());
        //支付宝配置
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(alipayAccountConfig.getAppId());
        aliPayConfig.setPrivateKey(alipayAccountConfig.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(alipayAccountConfig.getAliPayPublicKey());
        aliPayConfig.setNotifyUrl(alipayAccountConfig.getNotifyUrl());
        aliPayConfig.setReturnUrl(alipayAccountConfig.getReturnUrl());

        //支付类, 所有方法都在这个类里
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        bestPayService.setAliPayConfig(aliPayConfig);

        return bestPayService;
    }
}
