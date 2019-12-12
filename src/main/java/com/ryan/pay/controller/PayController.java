package com.ryan.pay.controller;

import com.lly835.bestpay.model.PayResponse;
import com.ryan.pay.service.impl.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Li
 * @date 2019/12/12
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount) {
        PayResponse payResponse = payService.create(orderId, amount);
        Map<String, String> map = new HashMap<>();
        map.put("codeUrl", payResponse.getCodeUrl());
        return new ModelAndView("create", map);
    }

    @PostMapping("/notify")
    public void 

}
