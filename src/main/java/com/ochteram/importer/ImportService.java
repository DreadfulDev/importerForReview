package com.ochteram.importer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ImportService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PaymentService paymentService;

    // Read file and execute payments using given account number as debtor
    public void importFile(String debtorAccount, MultipartFile file, String type) {
        String fileContent = null;
        try {
            fileContent = new String(file.getBytes());
        } catch (IOException e) {
            log.error("Cannot read file");
            return;
        }
        List<Payment> payments;
        if (type.equals("CSV")) {
            payments = parseCsv(fileContent);
        } else {
            payments = parseXml(fileContent);
        }

        //may return null
        BigDecimal accountBalance = accountService.getAccountBalance(debtorAccount);
        BigDecimal paymentsAmountSum = calculatePaymentsAmountSum(payments);
        if(paymentsAmountSum.compareTo(accountBalance) <= 0) {
            for(Payment payment : payments) {
                PaymentRequest request = new PaymentRequest(payment, debtorAccount);
                paymentService.makePayment(request);
            }
        }
    }

    private BigDecimal calculatePaymentsAmountSum(List<Payment> payments) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Payment payment : payments) {
            sum = sum.add(new BigDecimal(payment.getAmount()));
        }
        return sum;
    }

    private List<Payment> parseXml(String fileContent) {
        try {
            return new XmlMapper().readValue(fileContent, PaymentEnvelope.class).getPayments();
        } catch (JsonProcessingException e) {
            log.error("Invalid XML format");
        }
        return null;
    }

    private List<Payment> parseCsv(String fileContent) {
        try {
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            return new CsvMapper().readerFor(Payment.class).with(schema).<Payment>readValues(fileContent).readAll();
        } catch (IOException e) {
            log.error("Invalid CSV format");
        }
        return null;
    }
}
