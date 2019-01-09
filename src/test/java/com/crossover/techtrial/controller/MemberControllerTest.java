package com.crossover.techtrial.controller;

import static com.crossover.techtrial.model.MembershipStatus.ACTIVE;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import com.crossover.techtrial.dto.TopMemberDto;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
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
public class MemberControllerTest {

  private static final String API_MEMBER = "/api/member";

  @Mock
  private MemberController memberController;

  @Autowired
  private TestRestTemplate rest;

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
    MockMvcBuilders.standaloneSetup(memberController).build();
  }

  @After
  public void tearDown() {
    transactionRepository.deleteAll();
    bookRepository.deleteAll();
    memberRepository.deleteAll();
  }

  @Test
  public void saveFindAllFindById() throws Exception {
    // Arrange
    final String expectedMemberName = "test 1";
    final HttpEntity<String> newMember = getMemberHttpEntity(expectedMemberName);
    final ParameterizedTypeReference<List<Member>> membersListType =
        new ParameterizedTypeReference<List<Member>>() {
        };

    // Act
    final ResponseEntity<Member> saveResponse = rest
        .postForEntity(API_MEMBER, newMember, Member.class);
    final ResponseEntity<List<Member>> findAllResponse = rest
        .exchange(API_MEMBER, GET, null, membersListType);
    final Member savedMember = requireNonNull(saveResponse.getBody());
    final Long savedMemberId = savedMember.getId();
    final ResponseEntity<Member> findMemberByIdResponse = rest
        .getForEntity(API_MEMBER + "/" + savedMemberId, Member.class);

    // Assert
    assertEquals(OK.value(), saveResponse.getStatusCode().value());
    assertEquals(expectedMemberName, savedMember.getName());

    assertEquals(OK.value(), findAllResponse.getStatusCode().value());
    final List<Member> allMembers = requireNonNull(findAllResponse.getBody());
    assertEquals(1, allMembers.size());
    assertEquals(expectedMemberName, allMembers.get(0).getName());

    assertEquals(OK.value(), findMemberByIdResponse.getStatusCode().value());
    assertEquals(expectedMemberName, requireNonNull(findMemberByIdResponse.getBody()).getName());
  }

  @Test
  public void findById_NotFound() {
    // Act
    final ResponseEntity<Member> getMemberByIdResponse = rest
        .getForEntity(API_MEMBER + "/100500", Member.class);

    // Assert
    assertEquals(NOT_FOUND.value(), getMemberByIdResponse.getStatusCode().value());
  }

  @Test
  public void save_WrongEmail() throws Exception {
    // Arrange
    final HttpEntity<String> wrongMember1 = getMemberHttpEntity("member 1", "wrongEmail");
    final HttpEntity<String> wrongMember2 = getMemberHttpEntity("member 2", "");

    // Act
    final ResponseEntity<Member> registerResponse1 = rest.postForEntity(
        API_MEMBER, wrongMember1, Member.class);
    final ResponseEntity<Member> registerResponse2 = rest.postForEntity(
        API_MEMBER, wrongMember2, Member.class);

    // Assert
    assertEquals(BAD_REQUEST.value(), registerResponse1.getStatusCode().value());
    assertEquals(BAD_REQUEST.value(), registerResponse2.getStatusCode().value());
  }

  @Test
  public void save_NotUniqueEmail() throws Exception {
    // Arrange
    final HttpEntity<String> newMember1 = getMemberHttpEntity("test member 1");
    final HttpEntity<String> newMember2 = getMemberHttpEntity("test member 2");

    // Act
    final ResponseEntity<Member> registerResponse = rest.postForEntity(
        API_MEMBER, newMember1, Member.class);
    final ResponseEntity<Member> registerResponse2 = rest.postForEntity(
        API_MEMBER, newMember2, Member.class);

    // Assert
    assertEquals(OK.value(), registerResponse.getStatusCode().value());
    assertEquals(BAD_REQUEST.value(), registerResponse2.getStatusCode().value());
  }

  @Test
  public void findTop5() {
    // Arrange
    final LocalDateTime startDate = LocalDateTime.of(2019, 1, 9, 0, 0);
    final LocalDateTime endDate = startDate.plusDays(30);
    prepareTop5DbData(startDate, endDate);
    final String url = prepareUrl(startDate, endDate);
    final ParameterizedTypeReference<List<TopMemberDto>> typeRef =
        new ParameterizedTypeReference<List<TopMemberDto>>() {
        };

    // Act
    final ResponseEntity<List<TopMemberDto>> findTop5Response = rest
        .exchange(url, GET, null, typeRef);

    // Assert
    assertEquals(OK.value(), findTop5Response.getStatusCode().value());
    final List<TopMemberDto> top5 = requireNonNull(findTop5Response.getBody());
    assertEquals(5, top5.size());

    for (int i = 0; i < top5.size(); i++) {
      final TopMemberDto topMemberDto = top5.get(i);
      int expectedNumberOfBooks = 1;

      if (i == 0) {
        expectedNumberOfBooks = 3;
      } else if (i == 1) {
        expectedNumberOfBooks = 2;
      }

      assertEquals("Member " + i, topMemberDto.getMemberName());
      assertEquals(expectedNumberOfBooks, topMemberDto.getNumberOfBooks());
    }
  }

  private void prepareTop5DbData(LocalDateTime startDate, LocalDateTime endDate) {
    final int booksMembersNumber = 15;
    final Book[] books = new Book[booksMembersNumber];
    final Member[] members = new Member[booksMembersNumber];

    for (int i = 0; i < booksMembersNumber; i++) {
      books[i] = bookRepository.save(Book.builder().title("Book " + i).build());
      members[i] = memberRepository
          .save(Member.builder()
              .name("Member " + i)
              .email("test" + i + "@crossover.com")
              .membershipStartDate(LocalDateTime.now())
              .membershipStatus(ACTIVE)
              .build());
    }

    int memberNum = 0;
    for (int i = 0; i < 8; i++) {
      if (i == 3 || i >= 5) {
        ++memberNum;
      }

      transactionRepository.save(Transaction.builder().book(books[i]).member(members[memberNum])
          .dateOfIssue(startDate.plusDays(i)).dateOfReturn(endDate.minusDays(i)).build());
    }

    transactionRepository.save(
        Transaction.builder().book(books[8]).member(members[5]).dateOfIssue(startDate.minusDays(1))
            .dateOfReturn(endDate.plusDays(1)).build());
    transactionRepository.save(
        Transaction.builder().book(books[9]).member(members[6]).dateOfIssue(startDate)
            .dateOfReturn(endDate.plusDays(1)).build());
    transactionRepository.save(
        Transaction.builder().book(books[10]).member(members[7]).dateOfIssue(startDate.minusDays(1))
            .dateOfReturn(endDate).build());
    transactionRepository.save(
        Transaction.builder().book(books[11]).member(members[8]).dateOfIssue(startDate.plusDays(1))
            .dateOfReturn(endDate.plusDays(1)).build());
    transactionRepository.save(
        Transaction.builder().book(books[12]).member(members[9]).dateOfIssue(startDate.minusDays(1))
            .dateOfReturn(endDate.minusDays(1)).build());
    transactionRepository.save(
        Transaction.builder().book(books[13]).member(members[10]).dateOfIssue(startDate.plusDays(1))
            .build());
    transactionRepository.save(
        Transaction.builder().book(books[14]).member(members[11]).dateOfIssue(startDate.plusDays(1))
            .dateOfReturn(endDate.minusDays(1)).build());
  }

  private String prepareUrl(LocalDateTime startDate, LocalDateTime endDate) {
    final String fakeHost = "http://localhost";
    final String url = fakeHost + API_MEMBER + "/top-member";
    final String startDateStr = startDate.format(ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    final String endTimeStr = endDate.format(ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

    return fromHttpUrl(url)
        .queryParam("startTime", startDateStr)
        .queryParam("endTime", endTimeStr)
        .build()
        .encode()
        .toUriString()
        .replace(fakeHost, "");
  }

  @Test
  public void save_WrongName() throws Exception {
    // Arrange
    final HttpEntity<String> wrongMember1 = getMemberHttpEntity("");
    final HttpEntity<String> wrongMember2 = getMemberHttpEntity("m");
    final String longName = makeLongName();
    final HttpEntity<String> wrongMember3 = getMemberHttpEntity(longName);

    // Act
    final ResponseEntity<Member> registerResponse1 = rest.postForEntity(
        API_MEMBER, wrongMember1, Member.class);
    final ResponseEntity<Member> registerResponse2 = rest.postForEntity(
        API_MEMBER, wrongMember2, Member.class);
    final ResponseEntity<Member> registerResponse3 = rest.postForEntity(
        API_MEMBER, wrongMember3, Member.class);

    // Assert
    assertEquals(BAD_REQUEST.value(), registerResponse1.getStatusCode().value());
    assertEquals(BAD_REQUEST.value(), registerResponse2.getStatusCode().value());
    assertEquals(BAD_REQUEST.value(), registerResponse3.getStatusCode().value());
  }

  private String makeLongName() {
    final char[] longNameArr = new char[50];
    Arrays.fill(longNameArr, 'a');

    return Arrays.toString(longNameArr); // together with commas and brackets it'll be 101 character
  }

  private HttpEntity<String> getMemberHttpEntity(String name)
      throws JsonProcessingException {
    return getMemberHttpEntity(name, "test10000000000001@gmail.com");
  }

  private HttpEntity<String> getMemberHttpEntity(String name, String email)
      throws JsonProcessingException {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    final Member member = Member.builder()
        .name(name)
        .email(email)
        .membershipStatus(ACTIVE)
        .membershipStartDate(LocalDateTime.of(2018, 8, 8, 12, 12, 12))
        .build();

    return new HttpEntity<>(objectMapper.writeValueAsString(member), headers);
  }
}
