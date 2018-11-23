/**
 * 
 */
package com.crossover.techtrial.service;

import java.util.List;
import com.crossover.techtrial.model.Member;

/**
 * RideService for rides.
 * @author crossover
 *
 */
public interface MemberService {
  
  public Member save(Member member);
  
  public Member findById(Long memberId);
  
  public List<Member> findAll();
  
}
