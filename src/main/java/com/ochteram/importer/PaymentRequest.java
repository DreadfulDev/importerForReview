package com.ochteram.importer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {
    private Payment payment;
    private String debtorAccount;
}
