package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "The book is already returned")
@AllArgsConstructor
@Getter
@ToString
public class BookAlreadyReturnedException extends RuntimeException {

  private static final long serialVersionUID = -9159223549110310655L;

  private final Transaction transaction;
}
