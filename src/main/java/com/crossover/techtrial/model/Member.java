package com.crossover.techtrial.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

  private static final long serialVersionUID = -6893229160264523024L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  /**
   * Each member should have a valid unique email address. No two members can have the same email
   * address.
   */
  @Column(unique = true, nullable = false)
  // NOTE: perhaps it would be better to implement it as a trigger on the DB level
  @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
      + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
      + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
      message = "{invalid.email}")
  private String email;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private MembershipStatus membershipStatus;

  @Column(nullable = false)
  private LocalDateTime membershipStartDate;
}
