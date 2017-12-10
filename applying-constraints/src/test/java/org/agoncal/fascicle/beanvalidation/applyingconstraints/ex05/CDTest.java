package org.agoncal.fascicle.beanvalidation.applyingconstraints.ex05;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Antonio Goncalves
 * http://www.antoniogoncalves.org
 * --
 */
public class CDTest {

  // ======================================
  // =             Attributes             =
  // ======================================

  protected static ValidatorFactory vf;
  protected static Validator validator;

  // ======================================
  // =          Lifecycle Methods         =
  // ======================================

  @BeforeAll
  public static void init() {
    vf = Validation.buildDefaultValidatorFactory();
    validator = vf.getValidator();
  }

  @AfterAll
  public static void close() {
    vf.close();
  }

  // ======================================
  // =              Methods               =
  // ======================================

  @Test
  public void shouldRaiseNoConstraintViolation() {

    CD cd = new CD("Help", "EMI");
    cd.addTrack(1, "Help!");

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    assertEquals(0, violations.size());
  }

  @Test
  public void shouldRaiseViolationDueToEmpty() {

    CD cd = new CD("Help", "EMI");
    cd.addTrack(1, "");

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    assertEquals(1, violations.size());
  }

  @Test
  public void shouldRaiseViolationDueToNegative() {

    CD cd = new CD("Help", "EMI");
    cd.addTrack(-1, "Help!");

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    assertEquals(1, violations.size());
  }

  @Test
  public void shouldRaiseViolationDueToMax() {

    CD cd = new CD("Help", "EMI");
    cd.addTrack(1, "Help!");
    cd.addTrack(2, "The Night Before");
    cd.addTrack(3, "You've Got to Hide Your Love Away");
    cd.addTrack(4, "I Need You");
    cd.addTrack(5, "Another Girl");
    cd.addTrack(6, "You're Going to Lose That Girl");

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    assertEquals(1, violations.size());
  }

  @Test
  public void shouldRaiseViolationDueToEmptyMap() {

    CD cd = new CD("Help", "EMI");
    cd.setTracks(new HashMap<>());

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    assertEquals(1, violations.size());
  }

  @Test
  public void shouldRaiseViolationDueToNullMap() {

    CD cd = new CD("Help", "EMI");
    cd.setTracks(null);

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    assertEquals(1, violations.size());
  }

  private void displayContraintViolations(Set<ConstraintViolation<CD>> constraintViolations) {
    for (ConstraintViolation constraintViolation : constraintViolations) {
      System.out.println("### " + constraintViolation.getRootBeanClass().getSimpleName() +
        "." + constraintViolation.getPropertyPath() + " - Invalid Value = " + constraintViolation.getInvalidValue() + " - Error Msg = " + constraintViolation.getMessage());

    }
  }
}
