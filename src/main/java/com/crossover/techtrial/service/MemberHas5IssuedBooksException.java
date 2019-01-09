package com.crossover.techtrial.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Member already has 5 books issued on his name")
@AllArgsConstructor
@Getter
@ToString
public class MemberHas5IssuedBooksException extends RuntimeException {

  private static final long serialVersionUID = -5823287186169952210L;

  private final Long memberId;
}
