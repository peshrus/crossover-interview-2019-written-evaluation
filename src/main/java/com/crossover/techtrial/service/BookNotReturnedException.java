package com.crossover.techtrial.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Book not returned to be issued")
public class BookNotReturnedException extends RuntimeException {

  private static final long serialVersionUID = -9107149729067917175L;
}
