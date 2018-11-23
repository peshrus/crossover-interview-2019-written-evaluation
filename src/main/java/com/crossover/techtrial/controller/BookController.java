/**
 * 
 */
package com.crossover.techtrial.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;

/**
 * BookController for Book related APIs.
 * @author crossover
 *
 */
@RestController
public class BookController {
  
  @Autowired 
  private BookService bookService;
  
  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @GetMapping(path ="/api/book")
  public ResponseEntity<List<Book>> getBooks() {
    return ResponseEntity.ok(bookService.getAll());
  }

  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @PostMapping(path ="/api/book")
  public ResponseEntity<Book> saveBook(@RequestBody Book book) {
    return ResponseEntity.ok(bookService.save(book));
  }
  
  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @GetMapping(path = "/api/book/{book-id}")
  public ResponseEntity<Book> getRideById(@PathVariable(name="book-id",required=true)Long bookId){
    Book book = bookService.findById(bookId);
    if (book!=null)
      return ResponseEntity.ok(book);
    return ResponseEntity.notFound().build();
  }

  
}
