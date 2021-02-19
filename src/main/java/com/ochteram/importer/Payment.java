package com.ochteram.importer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payment {
    private String accountNumber;
    private String creditor;
    private String amount;
}
