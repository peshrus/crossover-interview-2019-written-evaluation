package com.crossover.techtrial.controller;

import java.util.Objects;
import org.junit.Assert;
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
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

  @Mock
  private MemberController memberController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  MemberRepository memberRepository;
  
  @Before
  public void setup() {
    MockMvcBuilders.standaloneSetup(memberController).build();
  }
  
  @Test
  public void testMemberRegistrationSuccessful() {
    HttpEntity<Object> member = getHttpEntity();
    
    ResponseEntity<Member> response = template.postForEntity(
        "/api/member", member, Member.class);
    
    Assert.assertEquals("test 1", Objects.requireNonNull(response.getBody()).getName());
    Assert.assertEquals(200,response.getStatusCode().value());
    
    //cleanup the user
    memberRepository.deleteById(response.getBody().getId());
  }

  private HttpEntity<Object> getHttpEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>("{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\","
        + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }", headers);
  }
}
