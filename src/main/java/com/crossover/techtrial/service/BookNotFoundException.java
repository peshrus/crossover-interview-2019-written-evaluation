package com.crossover.techtrial.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Book not found")
public class BookNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 7321250544763609613L;
}
