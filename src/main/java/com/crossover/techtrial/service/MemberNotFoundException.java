package com.crossover.techtrial.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Member not found")
@AllArgsConstructor
@Getter
@ToString
public class MemberNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 6642729657756170777L;

  private final Long memberId;
}
