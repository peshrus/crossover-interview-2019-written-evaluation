package com.crossover.techtrial.controller;

import static com.crossover.techtrial.controller.BookControllerTest.API_BOOK;
import static com.crossover.techtrial.controller.BookControllerTest.getBookHttpEntity;
import static com.crossover.techtrial.controller.MemberControllerTest.API_MEMBER;
import static com.crossover.techtrial.controller.MemberControllerTest.getMemberHttpEntity;
import static com.crossover.techtrial.controller.TransactionController.PARAM_BOOK_ID;
import static com.crossover.techtrial.controller.TransactionController.PARAM_MEMBER_ID;
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
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private BookController bookController;

  @Mock
  private MemberController memberController;

  @Mock
  private TransactionController transactionController;

  @Autowired
  private TestRestTemplate template;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setup() {
    MockMvcBuilders.standaloneSetup(bookController, memberController, transactionController)
        .build();
  }

  @After
  public void tearDown() {
    transactionRepository.deleteAll();
    bookRepository.deleteAll();
    memberRepository.deleteAll();
  }

  @Test
  public void issueAlreadyIssuedBook() throws Exception {
    // Arrange
    final HttpEntity<String> newBook = getBookHttpEntity(objectMapper, "test title");
    final HttpEntity<String> newMember1 = getMemberHttpEntity(objectMapper,
        "member 1", "test10000000000001@gmail.com");
    final HttpEntity<String> newMember2 = getMemberHttpEntity(objectMapper,
        "member 2", "test10000000000002@gmail.com");
    final ResponseEntity<Book> bookResponse = template
        .postForEntity(API_BOOK, newBook, Book.class);
    final ResponseEntity<Member> memberResponse1 = template
        .postForEntity(API_MEMBER, newMember1, Member.class);
    final ResponseEntity<Member> memberResponse2 = template
        .postForEntity(API_MEMBER, newMember2, Member.class);
    final Map<String, Long> transactionParams = new HashMap<>();

    transactionParams.put(PARAM_BOOK_ID, requireNonNull(bookResponse.getBody()).getId());
    transactionParams.put(PARAM_MEMBER_ID, requireNonNull(memberResponse1.getBody()).getId());

    // Act
    final ResponseEntity<Transaction> transactionResponse1 = template
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

    transactionParams.put(PARAM_MEMBER_ID, requireNonNull(memberResponse2.getBody()).getId());

    final ResponseEntity<Transaction> transactionResponse2 = template
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

    // Assert
    assertEquals(OK.value(), transactionResponse1.getStatusCode().value());
    assertEquals(FORBIDDEN.value(), transactionResponse2.getStatusCode().value());
  }

  @Test
  public void issueNotExistingBook() throws Exception {
    // Arrange
    final HttpEntity<String> newMember = getMemberHttpEntity(objectMapper,
        "test member", "test10000000000001@gmail.com");
    final ResponseEntity<Member> memberResponse = template
        .postForEntity(API_MEMBER, newMember, Member.class);
    final Map<String, Long> transactionParams = new HashMap<>();

    transactionParams.put(PARAM_BOOK_ID, 100500L);
    transactionParams.put(PARAM_MEMBER_ID, requireNonNull(memberResponse.getBody()).getId());

    // Act
    final ResponseEntity<Transaction> transactionResponse = template
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);

    // Assert
    assertEquals(NOT_FOUND.value(), transactionResponse.getStatusCode().value());
  }

  @Test
  public void doubleReturn() throws Exception {
    // Arrange
    final HttpEntity<String> newBook = getBookHttpEntity(objectMapper, "test title");
    final HttpEntity<String> newMember = getMemberHttpEntity(objectMapper,
        "return member", "test10000000000001@gmail.com");
    final ResponseEntity<Book> bookResponse = template
        .postForEntity(API_BOOK, newBook, Book.class);
    final ResponseEntity<Member> memberResponse = template
        .postForEntity(API_MEMBER, newMember, Member.class);
    final Map<String, Long> transactionParams = new HashMap<>();

    transactionParams.put(PARAM_BOOK_ID, requireNonNull(bookResponse.getBody()).getId());
    transactionParams.put(PARAM_MEMBER_ID, requireNonNull(memberResponse.getBody()).getId());

    final ResponseEntity<Transaction> issueResponse = template
        .postForEntity(API_TRANSACTION, transactionParams, Transaction.class);
    final Long transactionId = requireNonNull(issueResponse.getBody()).getId();
    final String url = API_TRANSACTION + "/" + transactionId + "/return";

    // Act
    final ResponseEntity<Transaction> returnResponse1 = template
        .exchange(url, PATCH, patch(), Transaction.class);
    final ResponseEntity<Transaction> returnResponse2 = template
        .exchange(url, PATCH, patch(), Transaction.class);

    // Assert
    assertEquals(OK.value(), returnResponse1.getStatusCode().value());
    assertEquals(FORBIDDEN.value(), returnResponse2.getStatusCode().value());
  }

  private static HttpEntity<Object> patch() {
    final HttpHeaders headers = new HttpHeaders();
    final MediaType mediaType = new MediaType("application", "merge-patch+json");
    headers.setContentType(mediaType);

    return new HttpEntity<>(headers);
  }
}