/**
 * 
 */
package com.crossover.techtrial.service;

import java.util.List;
import com.crossover.techtrial.model.Book;

/**
 * BookService interface for Books.
 * @author cossover
 *
 */
public interface BookService {
  
  public List<Book> getAll();
  
  public Book save(Book p);
  
  public Book findById(Long bookId);
  
}
