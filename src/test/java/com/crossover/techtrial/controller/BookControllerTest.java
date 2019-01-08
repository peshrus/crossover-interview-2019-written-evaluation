package com.crossover.techtrial.controller;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  static final String API_BOOK = "/api/book";

  @Mock
  private BookController bookController;

  @Autowired
  private TestRestTemplate template;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private ObjectMapper objectMapper;

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
  public void saveFindAllFindById() throws Exception {
    // Arrange
    final String expectedBookTitle = "test 1";
    final HttpEntity<String> newBook = getBookHttpEntity(objectMapper, expectedBookTitle);

    // Act
    final ResponseEntity<Book> saveResponse = template.postForEntity(API_BOOK, newBook, Book.class);
    final ResponseEntity<List> findAllResponse = template.getForEntity(API_BOOK, List.class);
    final Book savedBook = requireNonNull(saveResponse.getBody());
    final Long savedBookId = savedBook.getId();
    final ResponseEntity<Book> findByIdResponse = template
        .getForEntity(API_BOOK + "/" + savedBookId, Book.class);

    // Assert
    assertEquals(OK.value(), saveResponse.getStatusCode().value());
    assertEquals(expectedBookTitle, savedBook.getTitle());

    assertEquals(OK.value(), findAllResponse.getStatusCode().value());
    final List<HashMap<String, String>> allBooks = requireNonNull(findAllResponse.getBody());
    assertEquals(1, allBooks.size());
    assertEquals(expectedBookTitle, allBooks.get(0).get("title"));

    assertEquals(OK.value(), findByIdResponse.getStatusCode().value());
    final Book bookById = requireNonNull(findByIdResponse.getBody());
    assertEquals(expectedBookTitle, bookById.getTitle());
  }

  @Test
  public void findById_NotFound() {
    // Act
    final ResponseEntity<Book> getBookByIdResponse = template
        .getForEntity(API_BOOK + "/100500", Book.class);

    // Assert
    assertEquals(NOT_FOUND.value(), getBookByIdResponse.getStatusCode().value());
  }

  static HttpEntity<String> getBookHttpEntity(ObjectMapper objectMapper, String title)
      throws JsonProcessingException {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    final Book book = new Book();
    book.setTitle(title);

    return new HttpEntity<>(objectMapper.writeValueAsString(book), headers);
  }
}
