/**
 * 
 */
package com.crossover.techtrial.dto;

/**
 * @author crossover
 *
 */
public class TopMemberDTO {
  
  /**
   * Constructor for TopMemberDTO
   * @param memberId
   * @param name
   * @param email
   * @param bookCount
   */
  public TopMemberDTO(Long memberId,
      String name, 
      String email, 
      Integer bookCount) {
    this.name = name;
    this.email = email;
    this.memberId = memberId;
    this.bookCount = bookCount;
  }
  
  public TopMemberDTO() {
    
  }
  
  private Long memberId;
  
  private String name;
  
  private String email;
  
  private Integer bookCount;

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getBookCount() {
    return bookCount;
  }

  public void setBookCount(Integer bookCount) {
    this.bookCount = bookCount;
  }
}
