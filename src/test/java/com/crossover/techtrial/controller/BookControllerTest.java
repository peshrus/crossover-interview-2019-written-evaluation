package com.crossover.techtrial.controller;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

  private static final String API_BOOK = "/api/book";
  @Mock
  private BookController bookController;

  @Autowired
  private TestRestTemplate template;

  @Autowired
  private BookRepository bookRepository;

  @Before
  public void setup() {
    MockMvcBuilders.standaloneSetup(bookController).build();
  }

  @After
  public void tearDown() {
    bookRepository.deleteAll();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void saveBookGetAllBooksGetBookById() {
    // Arrange
    final HttpEntity<Object> newBook = getHttpEntity();

    // Act
    final ResponseEntity<Book> saveBookResponse = template.postForEntity(
        API_BOOK, newBook, Book.class);
    final ResponseEntity<List> getAllBooksResponse = template.getForEntity(API_BOOK, List.class);
    final Book savedBook = requireNonNull(saveBookResponse.getBody());
    final Long newBookId = savedBook.getId();
    final ResponseEntity<Book> getBookByIdResponse = template
        .getForEntity(API_BOOK + "/" + newBookId, Book.class);

    // Assert
    assertEquals(OK.value(), saveBookResponse.getStatusCode().value());
    final String expectedBookTitle = "test 1";
    assertEquals(expectedBookTitle, savedBook.getTitle());

    assertEquals(OK.value(), getAllBooksResponse.getStatusCode().value());
    final List<HashMap<String, String>> allBooks = requireNonNull(getAllBooksResponse.getBody());
    assertEquals(1, allBooks.size());
    assertEquals(expectedBookTitle, allBooks.get(0).get("title"));

    assertEquals(OK.value(), getBookByIdResponse.getStatusCode().value());
    final Book bookById = requireNonNull(getBookByIdResponse.getBody());
    assertEquals(expectedBookTitle, bookById.getTitle());
  }

  @Test
  public void getBookById_NotFound() {
    // Act
    final ResponseEntity<Book> getBookByIdResponse = template
        .getForEntity(API_BOOK + "/100500", Book.class);

    // Assert
    assertEquals(NOT_FOUND.value(), getBookByIdResponse.getStatusCode().value());
  }

  private HttpEntity<Object> getHttpEntity() {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new HttpEntity<>("{\"title\": \"test 1\" }",
        headers);
  }
}
