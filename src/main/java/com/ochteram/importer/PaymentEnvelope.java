package com.ochteram.importer;

import lombok.Data;

import java.util.List;

@Data
public class PaymentEnvelope {
    private List<Payment> payments;
}
