package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Person repository for basic operations on Person entity.
 */
@RestResource(exported = false)
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {

  Optional<Member> findById(Long id);

  List<Member> findAll();
}
