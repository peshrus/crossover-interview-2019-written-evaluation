package com.crossover.techtrial.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "The book is already returned")
public class BookAlreadyReturnedException extends RuntimeException {

  private static final long serialVersionUID = -8555925356298100753L;
}
