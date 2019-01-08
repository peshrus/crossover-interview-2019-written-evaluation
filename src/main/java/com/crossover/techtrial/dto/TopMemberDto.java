package com.crossover.techtrial.dto;

import lombok.Value;

@Value
public class TopMemberDto {

  private final Long memberId;

  private final String name;

  private final String email;

  private final Integer bookCount;
}
