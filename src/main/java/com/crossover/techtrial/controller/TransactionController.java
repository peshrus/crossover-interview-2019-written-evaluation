/**
 * 
 */
package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;

/**
 * @author kshah
 *
 */
@RestController
public class TransactionController {
  
  @Autowired TransactionRepository transactionRepository;
  
  @Autowired BookRepository bookRepository;
  
  @Autowired MemberRepository memberRepository;
  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   * Example Post Request :  { "bookId":1,"memberId":33 }
   */
  @PostMapping(path = "/api/transaction")
  public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params){
    
    Long bookId = params.get("bookId");
    Long memberId = params.get("memberId");
    Transaction transaction = new Transaction();
    transaction.setBook(bookRepository.findById(bookId).orElse(null));
    transaction.setMember(memberRepository.findById(memberId).get());
    transaction.setDateOfIssue(LocalDateTime.now());    
    return ResponseEntity.ok().body(transactionRepository.save(transaction));
  }
  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @PatchMapping(path= "/api/transaction/{transaction-id}/return")
  public ResponseEntity<Transaction> returnBookTransaction(@PathVariable(name="transaction-id") Long transactionId){
    Transaction transaction = transactionRepository.findById(transactionId).get();
    transaction.setDateOfReturn(LocalDateTime.now());
    return ResponseEntity.ok().body(transaction);
  }

}
