package com.ryan.pay.service;

import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * @author Ryan Li
 * @date 2019/12/12
 */
public interface IPayService {
    /**
     * 发起支付
     */
    PayResponse create(String orderId, BigDecimal amount);

    /**
     * 异步通知处理
     */
    String asyncNotify(String notifyData);
}
