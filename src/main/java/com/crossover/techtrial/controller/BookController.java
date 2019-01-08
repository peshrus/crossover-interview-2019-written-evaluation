package com.crossover.techtrial.controller;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/book")
public class BookController {

  // NOTE: A service layer should be added instead of the repository in case of complex logic on
  // selected entities
  private final BookRepository bookRepository;

  @Autowired
  public BookController(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @GetMapping
  public ResponseEntity<List<Book>> findAll() {
    return ok(bookRepository.findAll());
  }

  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @PostMapping
  public ResponseEntity<Book> save(@RequestBody Book book) {
    return ok(bookRepository.save(book));
  }

  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @GetMapping(path = "/{book-id}")
  public ResponseEntity<Book> findById(@PathVariable(name = "book-id") Long bookId) {
    final Optional<Book> book = bookRepository.findById(bookId);

    return book.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
  }
}
