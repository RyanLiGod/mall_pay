package com.ryan.pay.service.impl;

import com.ryan.pay.PayApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author Ryan Li
 * @date 2019/12/12
 */
public class PayServiceTest extends PayApplicationTests {

    @Autowired(required = false)
    private PayService payService;

    @Test
    public void create() {
        // 或 new BigDecimal("0.01")，不能使用 new BigDecimal(0.01)
        payService.create("12313423123", BigDecimal.valueOf(0.01));
    }
}