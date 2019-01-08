package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Member;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

  List<Member> findAll();
}
