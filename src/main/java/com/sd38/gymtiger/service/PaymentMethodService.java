package com.sd38.gymtiger.service;

import com.sd38.gymtiger.model.PaymentMethod;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethod> getAll();

    Page<PaymentMethod> getAll(Integer page);

    PaymentMethod add(PaymentMethod paymentMethod);

    PaymentMethod update(PaymentMethod paymentMethod, Integer id);

    PaymentMethod delete(Integer id);

    PaymentMethod getOne(Integer id);

    List<PaymentMethod> getAllBillPaymentMethod(Integer billId);
}
