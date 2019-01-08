package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Book not returned to be issued")
@AllArgsConstructor
@Getter
@ToString
public class BookNotReturnedException extends RuntimeException {

  private static final long serialVersionUID = 1729554283164018996L;

  private final Transaction transaction;
}
