package com.crossover.techtrial.controller;

import static com.crossover.techtrial.model.MembershipStatus.ACTIVE;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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
public class MemberControllerTest {

  static final String API_MEMBER = "/api/member";

  @Mock
  private MemberController memberController;

  @Autowired
  private TestRestTemplate template;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setup() {
    MockMvcBuilders.standaloneSetup(memberController).build();
  }

  @After
  public void tearDown() {
    memberRepository.deleteAll();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void saveFindAllFindById() throws Exception {
    // Arrange
    final String expectedMemberName = "test 1";
    final HttpEntity<String> newMember = getMemberHttpEntity(objectMapper, expectedMemberName,
        "test10000000000001@gmail.com");

    // Act
    final ResponseEntity<Member> saveResponse = template.postForEntity(
        API_MEMBER, newMember, Member.class);
    final ResponseEntity<List> findAllResponse = template.getForEntity(API_MEMBER, List.class);
    final Member registeredMember = requireNonNull(saveResponse.getBody());
    final Long registeredMemberId = registeredMember.getId();
    final ResponseEntity<Member> findMemberByIdResponse = template
        .getForEntity(API_MEMBER + "/" + registeredMemberId, Member.class);

    // Assert
    assertEquals(OK.value(), saveResponse.getStatusCode().value());
    assertEquals(expectedMemberName, registeredMember.getName());

    assertEquals(OK.value(), findAllResponse.getStatusCode().value());
    final List<HashMap<String, String>> allMembers = requireNonNull(findAllResponse.getBody());
    assertEquals(1, allMembers.size());
    assertEquals(expectedMemberName, allMembers.get(0).get("name"));

    assertEquals(OK.value(), findMemberByIdResponse.getStatusCode().value());
    final Member memberById = requireNonNull(findMemberByIdResponse.getBody());
    assertEquals(expectedMemberName, memberById.getName());
  }

  @Test
  public void findById_NotFound() {
    // Act
    final ResponseEntity<Member> getMemberByIdResponse = template
        .getForEntity(API_MEMBER + "/100500", Member.class);

    // Assert
    assertEquals(NOT_FOUND.value(), getMemberByIdResponse.getStatusCode().value());
  }

  @Test
  public void save_WrongEmail() throws Exception {
    // Arrange
    final HttpEntity<String> wrongMember1 = getMemberHttpEntity(objectMapper, "member 1",
        "wrongEmail");
    final HttpEntity<String> wrongMember2 = getMemberHttpEntity(objectMapper, "member 2",
        "");

    // Act
    final ResponseEntity<Member> registerResponse1 = template.postForEntity(
        API_MEMBER, wrongMember1, Member.class);
    final ResponseEntity<Member> registerResponse2 = template.postForEntity(
        API_MEMBER, wrongMember2, Member.class);

    // Assert
    assertEquals(BAD_REQUEST.value(), registerResponse1.getStatusCode().value());
    assertEquals(BAD_REQUEST.value(), registerResponse2.getStatusCode().value());
  }

  @Test
  public void save_NotUniqueEmail() throws Exception {
    // Arrange
    final HttpEntity<String> newMember = getMemberHttpEntity(objectMapper,
        "test member", "test10000000000001@gmail.com");

    // Act
    final ResponseEntity<Member> registerResponse = template.postForEntity(
        API_MEMBER, newMember, Member.class);
    final ResponseEntity<Member> registerResponse2 = template.postForEntity(
        API_MEMBER, newMember, Member.class);

    // Assert
    assertEquals(OK.value(), registerResponse.getStatusCode().value());
    assertEquals(BAD_REQUEST.value(), registerResponse2.getStatusCode().value());
  }

  static HttpEntity<String> getMemberHttpEntity(ObjectMapper objectMapper, String name,
      String email) throws JsonProcessingException {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    final Member member = new Member();
    member.setName(name);
    member.setEmail(email);
    member.setMembershipStatus(ACTIVE);
    member.setMembershipStartDate(LocalDateTime.of(2018, 8, 8, 12, 12, 12));

    return new HttpEntity<>(objectMapper.writeValueAsString(member), headers);
  }
}
