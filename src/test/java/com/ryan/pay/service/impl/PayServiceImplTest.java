package com.ryan.pay.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.ryan.pay.PayApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @author Ryan Li
 * @date 2019/12/12
 */
public class PayServiceImplTest extends PayApplicationTests {

    @Autowired
    private PayServiceImpl payServiceImpl;

    @Test
    public void create() {
        // 或 new BigDecimal("0.01")，不能使用 new BigDecimal(0.01)
        payServiceImpl.create("12313423123", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }
}