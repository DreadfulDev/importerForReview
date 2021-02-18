package com.ochteram.importer;

import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    public void makePayment(PaymentRequest paymentRequest);
}
