package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Transaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

  @Query("SELECT t FROM Transaction t WHERE t.book.id = :bookId AND t.dateOfReturn IS NULL")
  Optional<Transaction> findNotReturnedBook(@Param("bookId") Long bookId);

  // NOTE: perhaps it would be better to implement it as a trigger on the DB level
  @Query("SELECT CASE WHEN COUNT(t.book) >= 5 THEN true ELSE false END FROM Transaction t WHERE t.member.id = :memberId AND t.dateOfReturn IS NULL")
  boolean has5IssuedBooks(@Param("memberId") Long memberId);
}
