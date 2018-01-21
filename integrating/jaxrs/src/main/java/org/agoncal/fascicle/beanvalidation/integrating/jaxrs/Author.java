package org.agoncal.fascicle.beanvalidation.integrating.jaxrs;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// @formatter:off
// tag::adocSnippet[]
public class Author {

  @NotNull
  private Long id;
  @NotNull @Size(min = 2, max = 50)
  private String firstname;
  @NotNull
  private String surname;
  @Size(max = 2000)
  private String bio;
  @Email
  private String email;

  // Constructors, getters, setters
  // tag::adocSkip[]
  // @formatter:on
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  // end::adocSkip[]
}
// end::adocSnippet[]
