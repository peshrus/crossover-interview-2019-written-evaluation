package com.crossover.techtrial.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transaction not found")
public class TransactionNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 6949118607165214753L;
}
