package com.ochteram.importer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(ImportService.class)
class ImportServiceTest {
    String CSV ="\"accountNumber\",\"creditor\",\"amount\"\n" +
            "\"123 123 123\",\"Bruce Wayne\",\"100\"\n" +
            "\"321 321 321\",\"Tony Stark\",\"200\"";
    String XML = "<PaymentEnvelope>" +
                "<payments>" +
                    "<Payment>" +
                        "<accountNumber>111 111 111</accountNumber>" +
                        "<creditor>Joker</creditor>" +
                        "<amount>12</amount>" +
                    "</Payment>" +
                    "<Payment>" +
                        "<accountNumber>222 222 222</accountNumber>" +
                        "<creditor>Thanos</creditor>" +
                        "<amount>99</amount>" +
                    "</Payment>" +
                    "<Payment>" +
                        "<accountNumber>333 333 333</accountNumber>" +
                        "<creditor>Bowser</creditor>" +
                        "<amount>45</amount>" +
                    "</Payment>" +
            "</payments>" +
            "</PaymentEnvelope>";
    @MockBean
    PaymentService paymentService;
    @MockBean
    AccountService accountService;
    @Autowired
    ImportService importService;

    @BeforeEach
    public void setup() {
        when(accountService.getAccountBalance(anyString())).thenReturn(BigDecimal.valueOf(1000));
    }

    @Test
    public void canImportCSV() {
        MockMultipartFile file = new MockMultipartFile("payments.csv", CSV.getBytes());

        importService.importFile("000 000 000", file, "CSV");
        verify(paymentService, times(2)).makePayment(any());
    }

    @Test
    public void canImportXML() {
        MockMultipartFile file = new MockMultipartFile("payments.xml", XML.getBytes());

        importService.importFile("000 000 000", file, "XML");
        verify(paymentService, times(3)).makePayment(any());
    }

}
