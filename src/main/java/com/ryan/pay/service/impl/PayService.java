package com.ryan.pay.service.impl;

import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.ryan.pay.dao.PayInfoMapper;
import com.ryan.pay.enums.PayPlatformEnum;
import com.ryan.pay.pojo.PayInfo;
import com.ryan.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Ryan Li
 * @date 2019/12/12
 */
@Slf4j
@Service
public class PayService implements IPayService {

    @Autowired
    private BestPayService bestPayService;

    @Autowired(required = false)
    private PayInfoMapper payInfoMapper;

    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
        // 写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        payInfoMapper.insertSelective(payInfo);

        PayRequest payRequest = new PayRequest();
        payRequest.setOrderName("1202883-hello");
        payRequest.setOrderId(orderId);
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(bestPayTypeEnum);
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("response={}", payResponse);

        return payResponse;
    }

    @Override
    public String asyncNotify(String notifyData) {
        // 1.签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("asyncNotify={}", payResponse);
        // 2.金额校验（从数据库查订单）
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (payInfo == null) {
            // 比较严重（正常情况下是不会发生的）发出告警：钉钉、短信
            throw new RuntimeException("通过orderNo查询到的结果是null");
        }
        // 如果订单支付状态不是"已支付"
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS)) {
            // Double类型比较大小，精度不好控制
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                throw new RuntimeException("异步通知中的金额和数据库里的不一致，orderNo=" + payResponse.getOrderId());
            }

            // 3.修改订单支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }

        // TODO pay发送MQ消息，mall接收MQ消息

        // 4.告诉微信不要再通知了
        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return "success";
        }

        throw new RuntimeException("暂不支持的支付类型");
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        return payInfoMapper.selectByOrderNo((Long.parseLong(orderId)));
    }
}
