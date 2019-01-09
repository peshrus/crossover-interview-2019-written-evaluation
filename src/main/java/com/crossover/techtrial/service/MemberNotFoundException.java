package com.crossover.techtrial.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Member not found")
public class MemberNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 4064181386052128893L;
}
