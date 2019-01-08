package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;

public interface TransactionService {

  Transaction issueBookToMember(Long bookId, Long memberId);

  Transaction returnBook(Long transactionId);
}
