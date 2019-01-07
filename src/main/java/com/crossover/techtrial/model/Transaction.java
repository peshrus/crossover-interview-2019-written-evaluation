package com.crossover.techtrial.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "transaction")
@Data
public class Transaction implements Serializable {

  private static final long serialVersionUID = -406402853696113486L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @OneToOne
  @JoinColumn(name = "book_id", referencedColumnName = "id")
  private Book book;

  @OneToOne
  @JoinColumn(name = "member_id", referencedColumnName = "id")
  private Member member;

  /**
   * Date and time of issuance of this book
   */
  @Column(name = "date_of_issue")
  private LocalDateTime dateOfIssue;

  /**
   * Date and time of return of this book
   */
  @Column(name = "date_of_return")
  private LocalDateTime dateOfReturn;
}
