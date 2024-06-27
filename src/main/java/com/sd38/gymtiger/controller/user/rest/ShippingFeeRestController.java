package com.sd38.gymtiger.controller.user.rest;

import com.sd38.gymtiger.utils.GHNUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/shipping-fee")
public class ShippingFeeRestController {
    @Autowired
    private GHNUtil ghnUtil;

    @GetMapping("/get")
    public BigDecimal getShippingFee(@RequestParam String city,
                                     @RequestParam String district,
                                     @RequestParam String ward,
                                     @RequestParam Integer quantity){
        return ghnUtil.calculateShippingFee(city, district, ward, quantity);
    }
}
