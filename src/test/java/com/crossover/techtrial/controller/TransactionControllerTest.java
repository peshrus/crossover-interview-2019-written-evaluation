package com.crossover.techtrial.controller;

import static com.crossover.techtrial.controller.TransactionController.PARAM_BOOK_ID;
import static com.crossover.techtrial.controller.TransactionController.PARAM_MEMBER_ID;
import static com.crossover.techtrial.model.MembershipStatus.ACTIVE;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
public class TransactionControllerTest {

  private static final String API_TRANSACTION = "/api/transaction";

  @Mock
  private TransactionController transactionController;

  @Autowired
  private TestRestTemplate rest;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Before
  public void setup() {
    MockMvcBuilders.standaloneSetup(transactionController).build();
  }

  @After
  public void tearDown() {
    transactionRepository.deleteAll();
    bookRepository.deleteAll();
    memberRepository.deleteAll();
  }

  @Test
  public void issueBookToMember_IssuedBook() {
    // Arrange
    final Book newBook = makeAndSaveBook("test title");
    final Member newMember1 = makeAndSaveMember("member 1", "test10000000000001@gmail.com");
    final Member newMember2 = makeAndSaveMember("member 2", "test10000000000002@gmail.com");
    final Map<String, Long> transactionParams = makeTransactionParams(newBook.getId(),
        newMember1.getId());

    // Act
    final ResponseEntity<Transaction> transactionResponse1 = rest
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

    transactionParams.put(PARAM_MEMBER_ID, newMember2.getId());
    final ResponseEntity<Transaction> transactionResponse2 = rest
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

    // Assert
    assertEquals(OK.value(), transactionResponse1.getStatusCode().value());
    assertEquals(FORBIDDEN.value(), transactionResponse2.getStatusCode().value());
  }

  private Member makeAndSaveMember(String name, String email) {
    return memberRepository.save(Member.builder()
        .name(name)
        .email(email)
        .membershipStatus(ACTIVE)
        .membershipStartDate(LocalDateTime.now())
        .build());
  }

  private Map<String, Long> makeTransactionParams(Long bookId, Long memberId) {
    final Map<String, Long> transactionParams = new HashMap<>();

    transactionParams.put(PARAM_BOOK_ID, bookId);
    transactionParams.put(PARAM_MEMBER_ID, memberId);

    return transactionParams;
  }

  @Test
  public void issueBookToMember_BookNotFound() {
    // Arrange
    final Member newMember = makeAndSaveMember("test member", "test10000000000001@gmail.com");
    final Map<String, Long> transactionParams = makeTransactionParams(100500L, newMember.getId());

    // Act
    final ResponseEntity<Transaction> transactionResponse = rest
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

    // Assert
    assertEquals(NOT_FOUND.value(), transactionResponse.getStatusCode().value());
  }

  @Test
  public void issueBookToMember_MemberNotFound() {
    // Arrange
    final Book savedBook = makeAndSaveBook("Test Book");
    final Map<String, Long> transactionParams = makeTransactionParams(savedBook.getId(), 100500L);

    // Act
    final ResponseEntity<Transaction> transactionResponse = rest
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

    // Assert
    assertEquals(NOT_FOUND.value(), transactionResponse.getStatusCode().value());
  }

  private Book makeAndSaveBook(String title) {
    return bookRepository.save(Book.builder().title(title).build());
  }

  @Test
  public void returnBook_TransactionNotFound() {
    // Act
    final ResponseEntity<Transaction> returnResponse = rest
        .exchange(API_TRANSACTION + "/100500/return", PATCH, patch(), Transaction.class);

    // Assert
    assertEquals(NOT_FOUND.value(), returnResponse.getStatusCode().value());
  }

  @Test
  public void returnBook_Twice() {
    // Arrange
    final Book newBook = makeAndSaveBook("test title");
    final Member newMember = makeAndSaveMember("return member", "test10000000000001@gmail.com");
    final Map<String, Long> transactionParams = makeTransactionParams(newBook.getId(),
        newMember.getId());

    final ResponseEntity<Transaction> issueResponse = rest
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);
    final Long transactionId = requireNonNull(issueResponse.getBody()).getId();
    final String url = API_TRANSACTION + "/" + transactionId + "/return";

    // Act
    final ResponseEntity<Transaction> returnResponse1 = rest
        .exchange(url, PATCH, patch(), Transaction.class);
    final ResponseEntity<Transaction> returnResponse2 = rest
        .exchange(url, PATCH, patch(), Transaction.class);

    // Assert
    assertEquals(OK.value(), returnResponse1.getStatusCode().value());
    assertEquals(FORBIDDEN.value(), returnResponse2.getStatusCode().value());
  }

  @Test
  public void issueMoreThan5Books() {
    // Arrange
    final Member savedMember = makeAndSaveMember("Test Member", "test@crossover.com");

    for (int i = 0; i < 6; i++) {
      final Book savedBook = makeAndSaveBook("Book " + i);
      final Map<String, Long> transactionParams = makeTransactionParams(savedBook.getId(),
          savedMember.getId());

      // Act
      final ResponseEntity<Transaction> transactionResponse = rest
          .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

      // Assert
      if (i < 5) {
        assertEquals(OK.value(), transactionResponse.getStatusCode().value());
      } else {
        assertEquals(FORBIDDEN.value(), transactionResponse.getStatusCode().value());
      }
    }
  }

  private static HttpEntity<Object> patch() {
    final HttpHeaders headers = new HttpHeaders();
    final MediaType mediaType = new MediaType("application", "merge-patch+json");
    headers.setContentType(mediaType);

    return new HttpEntity<>(headers);
  }
}