package com.crossover.techtrial.controller;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
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

  private static final String API_MEMBER = "/api/member";

  @Mock
  private MemberController memberController;

  @Autowired
  private TestRestTemplate template;

  @Autowired
  private MemberRepository memberRepository;

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
  public void registerGetAllGetMemberById() {
    // Arrange
    final HttpEntity<Object> newMember = getHttpEntity();

    // Act
    final ResponseEntity<Member> registerResponse = template.postForEntity(
        API_MEMBER, newMember, Member.class);
    final ResponseEntity<List> getAllResponse = template.getForEntity(API_MEMBER, List.class);
    final Member registeredMember = requireNonNull(registerResponse.getBody());
    final Long newMemberId = registeredMember.getId();
    final ResponseEntity<Member> getMemberByIdResponse = template
        .getForEntity(API_MEMBER + "/" + newMemberId, Member.class);

    // Assert
    assertEquals(OK.value(), registerResponse.getStatusCode().value());
    final String expectedMemberName = "test 1";
    assertEquals(expectedMemberName, registeredMember.getName());

    assertEquals(OK.value(), getAllResponse.getStatusCode().value());
    final List<HashMap<String, String>> allMembers = requireNonNull(getAllResponse.getBody());
    assertEquals(1, allMembers.size());
    assertEquals(expectedMemberName, allMembers.get(0).get("name"));

    assertEquals(OK.value(), getMemberByIdResponse.getStatusCode().value());
    final Member memberById = requireNonNull(getMemberByIdResponse.getBody());
    assertEquals(expectedMemberName, memberById.getName());
  }

  @Test
  public void getMemberById_NotFound() {
    // Act
    final ResponseEntity<Member> getMemberByIdResponse = template
        .getForEntity(API_MEMBER + "/100500", Member.class);

    // Assert
    assertEquals(NOT_FOUND.value(), getMemberByIdResponse.getStatusCode().value());
  }

  private HttpEntity<Object> getHttpEntity() {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new HttpEntity<>("{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\","
        + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }",
        headers);
  }
}
