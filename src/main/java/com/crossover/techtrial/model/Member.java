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
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "member")
@Data
public class Member implements Serializable {

  private static final long serialVersionUID = 2665432587130651197L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @Enumerated(EnumType.STRING)
  private MembershipStatus membershipStatus;

  @Column(name = "membership_start_date")
  private LocalDateTime membershipStartDate;
}
