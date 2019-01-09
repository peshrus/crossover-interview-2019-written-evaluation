package com.crossover.techtrial.dto;

import lombok.Value;

// API should return member name, a number of books issued/returned in this duration.
@Value
public class TopMemberDto {

  private final String memberName;

  private final long numberOfBooks;
}
