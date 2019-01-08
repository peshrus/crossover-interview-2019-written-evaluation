package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TopMemberDto;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/member")
public class MemberController {

  // NOTE: A service layer should be added instead of the repository in case of complex logic on
  // selected entities
  private final MemberRepository memberRepository;

  @Autowired
  public MemberController(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  /*
   * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @PostMapping
  public ResponseEntity<Member> save(@RequestBody Member p) {
    return ResponseEntity.ok(memberRepository.save(p));
  }

  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @GetMapping
  public ResponseEntity<List<Member>> findAll() {
    return ResponseEntity.ok(memberRepository.findAll());
  }

  /*
   * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
   */
  @GetMapping(path = "/{member-id}")
  public ResponseEntity<Member> findById(@PathVariable(name = "member-id") Long memberId) {
    final Optional<Member> member = memberRepository.findById(memberId);

    return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }


  /**
   * This API returns the top 5 members who issued the most books within the search duration.
   * Only books that have dateOfIssue and dateOfReturn within the mentioned duration should be
   * counted.
   * Any issued book where dateOfIssue or dateOfReturn is outside the search, should not be
   * considered.
   * <p>
   * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
   *
   * @return top 5 members who issued the most books within the search duration
   */
  @GetMapping(path = "/top-member")
  public ResponseEntity<List<TopMemberDto>> getTopMembers(
      @RequestParam(value = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
      @RequestParam(value = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
    List<TopMemberDto> topDrivers = new ArrayList<>();
    /* Your Implementation Here. */

    return ResponseEntity.ok(topDrivers);

  }
}
