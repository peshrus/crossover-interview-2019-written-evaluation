package com.crossover.techtrial.controller;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.core.ParameterizedTypeReference;
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
  private TestRestTemplate rest;

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
  public void saveFindAllFindById() throws Exception {
    // Arrange
    final String expectedBookTitle = "test 1";
    final HttpEntity<String> newBook = getBookHttpEntity(expectedBookTitle);
    final ParameterizedTypeReference<List<Book>> booksListType =
        new ParameterizedTypeReference<List<Book>>() {
        };

    // Act
    final ResponseEntity<Book> saveResponse = rest.postForEntity(API_BOOK, newBook, Book.class);
    final ResponseEntity<List<Book>> findAllResponse = rest
        .exchange(API_BOOK, GET, null, booksListType);
    final Book savedBook = requireNonNull(saveResponse.getBody());
    final Long savedBookId = savedBook.getId();
    final ResponseEntity<Book> findByIdResponse = rest
        .getForEntity(API_BOOK + "/" + savedBookId, Book.class);

    // Assert
    assertEquals(OK.value(), saveResponse.getStatusCode().value());
    assertEquals(expectedBookTitle, savedBook.getTitle());

    assertEquals(OK.value(), findAllResponse.getStatusCode().value());
    final List<Book> allBooks = requireNonNull(findAllResponse.getBody());
    assertEquals(1, allBooks.size());
    assertEquals(expectedBookTitle, allBooks.get(0).getTitle());

    assertEquals(OK.value(), findByIdResponse.getStatusCode().value());
    assertEquals(expectedBookTitle, requireNonNull(findByIdResponse.getBody()).getTitle());
  }

  @Test
  public void findById_NotFound() {
    // Act
    final ResponseEntity<Book> getBookByIdResponse = rest
        .getForEntity(API_BOOK + "/100500", Book.class);

    // Assert
    assertEquals(NOT_FOUND.value(), getBookByIdResponse.getStatusCode().value());
  }

  @SuppressWarnings("SameParameterValue")
  private HttpEntity<String> getBookHttpEntity(String title) throws JsonProcessingException {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    final Book book = Book.builder().title(title).build();

    return new HttpEntity<>(objectMapper.writeValueAsString(book), headers);
  }
}
