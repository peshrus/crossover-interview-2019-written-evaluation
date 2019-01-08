package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.TransactionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionController {

  static final String PARAM_BOOK_ID = "bookId";
  static final String PARAM_MEMBER_ID = "memberId";

  private final TransactionService transactionService;

  @Autowired
  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   * Example Post Request :  { "bookId":1,"memberId":33 }
   */
  @PostMapping
  public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {
    final Long bookId = params.get(PARAM_BOOK_ID);
    final Long memberId = params.get(PARAM_MEMBER_ID);
    final Transaction result = transactionService.issueBookToMember(bookId, memberId);

    return ResponseEntity.ok().body(result);
  }

  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @PatchMapping(path = "/{transaction-id}/return")
  public ResponseEntity<Transaction> returnBookTransaction(
      @PathVariable(name = "transaction-id") Long transactionId) {
    final Transaction result = transactionService.returnBook(transactionId);

    return ResponseEntity.ok().body(result);
  }
}
