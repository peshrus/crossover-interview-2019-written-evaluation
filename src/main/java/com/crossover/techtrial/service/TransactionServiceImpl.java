package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  private final BookRepository bookRepository;

  private final MemberRepository memberRepository;

  @Autowired
  public TransactionServiceImpl(
      TransactionRepository transactionRepository,
      BookRepository bookRepository,
      MemberRepository memberRepository) {
    this.transactionRepository = transactionRepository;
    this.bookRepository = bookRepository;
    this.memberRepository = memberRepository;
  }

  @Override
  public Transaction issueBookToMember(Long bookId, Long memberId) {
    final Book book = findAndCheckBook(bookId);
    checkBookReturned(bookId);
    final Member member = findAndCheckMember(memberId);

    // When issuing a book to a member, the application should update date of issuance
    final Transaction transaction = Transaction.builder()
        .book(book)
        .member(member)
        .dateOfIssue(LocalDateTime.now())
        .build();

    return transactionRepository.save(transaction);
  }

  private Book findAndCheckBook(Long bookId) {
    final Optional<Book> bookOptional = bookRepository.findById(bookId);

    if (!bookOptional.isPresent()) {
      // Member trying to issue a book which does not exist in our database, API should return
      // HTTP Status code 404.
      throw new BookNotFoundException(bookId);
    }

    return bookOptional.get();
  }

  private void checkBookReturned(Long bookId) {
    final Optional<Transaction> notReturnedBook = transactionRepository
        .findNotReturnedBook(bookId);

    if (notReturnedBook.isPresent()) {
      // Member is not allowed to issue a book which is already issued to someone and should
      // return HTTP Status code 403.
      throw new BookNotReturnedException(notReturnedBook.get());
    }
  }

  private Member findAndCheckMember(Long memberId) {
    final Optional<Member> memberOptional = memberRepository.findById(memberId);

    if (!memberOptional.isPresent()) {
      throw new MemberNotFoundException(memberId);
    }

    return memberOptional.get();
  }

  @Override
  public Transaction returnBook(Long transactionId) {
    final Transaction transaction = findAndCheckTransaction(transactionId);
    checkBookNotReturned(transaction);

    // on return, dateOfReturn should be recorded automatically
    transaction.setDateOfReturn(LocalDateTime.now());

    return transactionRepository.save(transaction);
  }

  private Transaction findAndCheckTransaction(Long transactionId) {
    final Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);

    if (!transactionOptional.isPresent()) {
      throw new TransactionNotFoundException(transactionId);
    }

    return transactionOptional.get();
  }

  private void checkBookNotReturned(Transaction transaction) {
    if (transaction.getDateOfReturn() != null) {
      // After returning the book and completing the transaction by updating date of return, Any
      // subsequent request to return for the same transaction-id should return HTTP Status Code
      // 403. Valid value of Date Of Return field means books are returned.
      throw new BookAlreadyReturnedException(transaction);
    }
  }
}
