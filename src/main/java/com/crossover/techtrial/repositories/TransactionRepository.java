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
}
