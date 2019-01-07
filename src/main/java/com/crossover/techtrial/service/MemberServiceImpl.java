package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Autowired
  public MemberServiceImpl(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public Member save(Member member) {
    return memberRepository.save(member);
  }

  public Member findById(Long memberId) {
    Optional<Member> optionalMember = memberRepository.findById(memberId);
    return optionalMember.orElse(null);
  }

  public List<Member> findAll() {
    return memberRepository.findAll();
  }
}
