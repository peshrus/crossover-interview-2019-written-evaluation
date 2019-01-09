package com.crossover.techtrial.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Member already has 5 books issued on his name")
public class MemberHas5IssuedBooksException extends RuntimeException {

  private static final long serialVersionUID = 120754165877366832L;
}
