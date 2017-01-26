package com.clouway.nvuapp.core;

import com.google.common.base.Objects;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */


public class Tutor {
  public final String tutorId;

  public final String password;

  public Tutor(String tutorId, String password) {
    this.tutorId = tutorId;
    this.password = password;
  }

  public String getTutorId() {
    return tutorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tutor tutor = (Tutor) o;
    return Objects.equal(tutorId, tutor.tutorId) &&
            Objects.equal(password, tutor.password);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tutorId, password);
  }
}
