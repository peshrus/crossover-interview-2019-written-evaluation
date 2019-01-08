package com.crossover.techtrial.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transaction not found")
@AllArgsConstructor
@Getter
@ToString
public class TransactionNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 5634917059114371909L;

  private final Long transactionId;
}
