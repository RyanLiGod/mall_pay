package com.ryan.pay.controller;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.ryan.pay.config.WxpayAccountConfig;
import com.ryan.pay.pojo.PayInfo;
import com.ryan.pay.service.impl.PayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Li
 * @date 2019/12/12
 */
@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayServiceImpl payServiceImpl;

    @Autowired
    private WxpayAccountConfig wxpayAccountConfig;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum) {
        PayResponse payResponse = payServiceImpl.create(orderId, amount, bestPayTypeEnum);
        Map<String, String> map = new HashMap<>();
        // 支付方式不同，渲染就不同，WXPAY_NATIVE使用codeUrl，ALIPAY_PC使用body
        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE) {
            map.put("codeUrl", payResponse.getCodeUrl());
            map.put("orderId", orderId);
            map.put("returnUrl", wxpayAccountConfig.getReturnUrl());
            return new ModelAndView("createWxpay", map);
        } else if (bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC) {
            map.put("body", payResponse.getBody());
            return new ModelAndView("createAlipay", map);
        }

        throw new RuntimeException("暂不支持的支付类型");
    }

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
        log.info("notifyData={}", notifyData);
        return payServiceImpl.asyncNotify(notifyData);
    }

    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(@RequestParam String orderId) {
        return payServiceImpl.queryByOrderId(orderId);
    }

}
