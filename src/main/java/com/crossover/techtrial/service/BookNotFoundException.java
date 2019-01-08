package com.crossover.techtrial.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Book not found")
@AllArgsConstructor
@Getter
@ToString
public class BookNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 749270910885405793L;

  private final Long bookId;
}
