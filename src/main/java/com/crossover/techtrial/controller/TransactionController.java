package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

  private final TransactionRepository transactionRepository;

  private final BookRepository bookRepository;

  private final MemberRepository memberRepository;

  @Autowired
  public TransactionController(TransactionRepository transactionRepository,
      BookRepository bookRepository, MemberRepository memberRepository) {
    this.transactionRepository = transactionRepository;
    this.bookRepository = bookRepository;
    this.memberRepository = memberRepository;
  }

  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   * Example Post Request :  { "bookId":1,"memberId":33 }
   */
  @PostMapping(path = "/api/transaction")
  public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {
    Long bookId = params.get("bookId");
    Long memberId = params.get("memberId");
    Transaction transaction = new Transaction();
    transaction.setBook(bookRepository.findById(bookId).orElse(null));
    transaction.setMember(memberRepository.findById(memberId).orElse(null));
    transaction.setDateOfIssue(LocalDateTime.now());
    return ResponseEntity.ok().body(transactionRepository.save(transaction));
  }

  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @PatchMapping(path = "/api/transaction/{transaction-id}/return")
  public ResponseEntity<Transaction> returnBookTransaction(
      @PathVariable(name = "transaction-id") Long transactionId) {
    Optional<Transaction> transaction = transactionRepository.findById(transactionId);
    transaction.ifPresent(t -> t.setDateOfReturn(LocalDateTime.now()));
    return ResponseEntity.ok().body(transaction.orElse(null));
  }
}
