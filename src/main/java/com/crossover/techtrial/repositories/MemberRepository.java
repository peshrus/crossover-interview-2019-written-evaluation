package com.crossover.techtrial.repositories;

import com.crossover.techtrial.dto.TopMemberDto;
import com.crossover.techtrial.model.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

  List<Member> findAll();

  /**
   * @return top members who completed the maximum number of
   *     transactions(issued/returned books) within the given duration. Completed transaction means
   *     that date of issuance and date of return are within the search range. API should return
   *     member name,a number of books issued/returned in this duration.
   */
  @Query("SELECT new com.crossover.techtrial.dto.TopMemberDto(m.name, COUNT(t.id)) "
      + "FROM Transaction t INNER JOIN t.member m "
      + "WHERE t.dateOfIssue >= :startTime AND t.dateOfReturn <= :endTime "
      + "GROUP BY m.id "
      + "ORDER BY COUNT(t.id) DESC, m.id ASC")
  List<TopMemberDto> findTopMembers(@Param("startTime") LocalDateTime startTime,
      @Param("endTime") LocalDateTime endTime, Pageable pageable);
}
