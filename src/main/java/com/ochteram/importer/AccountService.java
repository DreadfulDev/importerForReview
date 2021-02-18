package com.ochteram.importer;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface AccountService {
    public BigDecimal getAccountBalance(String accountNumber);
}
