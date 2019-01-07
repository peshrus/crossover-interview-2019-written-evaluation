package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  @Autowired
  public BookServiceImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public List<Book> getAll() {
    List<Book> personList = new ArrayList<>();
    bookRepository.findAll().forEach(personList::add);
    return personList;

  }

  public Book save(Book p) {
    return bookRepository.save(p);
  }

  @Override
  public Book findById(Long bookId) {
    Optional<Book> dbPerson = bookRepository.findById(bookId);
    return dbPerson.orElse(null);
  }
}
