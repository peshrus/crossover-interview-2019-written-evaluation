/**
 * 
 */
package com.crossover.techtrial.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author kshah
 *
 */
@Entity
@Table(name = "member")
public class Member implements Serializable{
  
  private static final long serialVersionUID = 9045098179799205444L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  String name;

  @Column(name = "email")
  String email;
  
  @Enumerated(EnumType.STRING)
  MembershipStatus membershipStatus;
  
  @Column(name = "membership_start_date")
  LocalDateTime membershipStartDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public MembershipStatus getMembershipStatus() {
    return membershipStatus;
  }

  public void setMembershipStatus(MembershipStatus membershipStatus) {
    this.membershipStatus = membershipStatus;
  }

  public LocalDateTime getMembershipStartDate() {
    return membershipStartDate;
  }

  public void setMembershipStartDate(LocalDateTime membershipStartDate) {
    this.membershipStartDate = membershipStartDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Member other = (Member) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Member [id=" + id + ", name=" + name + ", email=" + email + "]";
  }
  
  
  

}
