package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Book;
import java.util.List;

/**
 * BookService interface for Books.
 */
public interface BookService {

  List<Book> getAll();

  Book save(Book p);

  Book findById(Long bookId);
}
