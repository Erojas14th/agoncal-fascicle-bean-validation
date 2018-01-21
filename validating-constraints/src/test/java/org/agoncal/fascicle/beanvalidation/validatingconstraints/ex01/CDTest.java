package org.agoncal.fascicle.beanvalidation.validatingconstraints.ex01;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
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

  private static ValidatorFactory vf;
  private static Validator validator;

  // ======================================
  // =          Lifecycle Methods         =
  // ======================================

  @BeforeAll
  static void init() {
    vf = Validation.buildDefaultValidatorFactory();
    validator = vf.getValidator();
  }

  @AfterAll
  static void close() {
    vf.close();
  }

  // ======================================
  // =              Methods               =
  // ======================================

  @Test
  void shouldRaiseNoConstraintViolation() {

    // tag::shouldRaiseNoConstraintViolation[]
    CD cd = new CD().title("Kind of Blue").price(12.5f);

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    displayConstraintViolations(violations);
    assertEquals(0, violations.size());
    // end::shouldRaiseNoConstraintViolation[]
  }

  @Test
  void shouldRaiseConstraintViolationCauseTitleAndPriceAreNull() {

    // tag::shouldRaiseConstraintViolationCauseTitleAndPriceAreNull[]
    CD cd = new CD();

    Set<ConstraintViolation<CD>> violations = validator.validate(cd);
    assertEquals(2, violations.size());
    // end::shouldRaiseConstraintViolationCauseTitleAndPriceAreNull[]
  }

  @Test
  void shouldRaiseNoConstraintViolationValidatingNumberOfCDsProperty() {

    // tag::shouldRaiseNoConstraintViolationValidatingNumberOfCDsProperty[]
    CD cd = new CD().numberOfCDs(2);

    Set<ConstraintViolation<CD>> violations = validator.validateProperty(cd, "numberOfCDs");
    assertEquals(0, violations.size());
    // end::shouldRaiseNoConstraintViolationValidatingNumberOfCDsProperty[]
  }

  @Test //@Ignore("Make sure your local is EN, if not use the following JVM parameters : -Duser.language=en -Duser.country=EN")
  void shouldRaiseConstraintViolationValidatingNumberOfCDsProperty() {

    // tag::shouldRaiseConstraintViolationValidatingNumberOfCDsProperty[]
    CD cd = new CD().numberOfCDs(7);

    Set<ConstraintViolation<CD>> violations = validator.validateProperty(cd, "numberOfCDs");

    assertEquals(1, violations.size());
    ConstraintViolation<CD> violation = violations.iterator().next();

    assertEquals("must be less than or equal to 5", violation.getMessage());
    assertEquals(7, violation.getInvalidValue());
    assertEquals("{javax.validation.constraints.Max.message}", violation.getMessageTemplate());
    // end::shouldRaiseConstraintViolationValidatingNumberOfCDsProperty[]
  }

  @Test
  void shouldRaiseNoConstraintViolationValidatingNumberOfCDsPropertyValue() {

    Set<ConstraintViolation<CD>> violations;
    // tag::shouldRaiseNoConstraintViolationValidatingNumberOfCDsPropertyValue[]
    violations = validator.validateValue(CD.class, "numberOfCDs", 2);
    assertEquals(0, violations.size());

    violations = validator.validateValue(CD.class, "numberOfCDs", 7);
    assertEquals(1, violations.size());
    // end::shouldRaiseNoConstraintViolationValidatingNumberOfCDsPropertyValue[]
  }

  @Test
  void shouldRaiseNoMethodParameterConstraintViolation() throws NoSuchMethodException {

    // tag::shouldRaiseNoMethodParameterConstraintViolation[]
    CD cd = new CD().title("Kind of Blue").price(12.5f);

    ExecutableValidator methodValidator = validator.forExecutables();
    Method method = CD.class.getMethod("calculatePrice", Float.class);
    Set<ConstraintViolation<CD>> violations = methodValidator.validateParameters(cd, method, new Object[]{new Float(1.2)});
    assertEquals(1, violations.size());
    // end::shouldRaiseNoMethodParameterConstraintViolation[]
  }

  @Test //@Ignore("Make sure your local is EN, if not use the following JVM parameters : -Duser.language=en -Duser.country=EN")
  void shouldRaiseMethodParameterConstraintViolationCauseRateIsLow() throws NoSuchMethodException {

    CD cd = new CD().title("Kind of Blue").price(12.5f);

    ExecutableValidator methodValidator = validator.forExecutables();
    Method method = CD.class.getMethod("calculatePrice", Float.class);
    Set<ConstraintViolation<CD>> violations = methodValidator.validateParameters(cd, method, new Object[]{new Float(1.2)});
    displayConstraintViolations(violations);
    assertEquals(1, violations.size());
    assertEquals("must be greater than or equal to 1.4", violations.iterator().next().getMessage());
    assertEquals(new Float(1.2), violations.iterator().next().getInvalidValue());
    assertEquals("{javax.validation.constraints.DecimalMin.message}", violations.iterator().next().getMessageTemplate());
  }

  private void displayConstraintViolations(Set<ConstraintViolation<CD>> constraintViolations) {
    for (ConstraintViolation constraintViolation : constraintViolations) {
      System.out.println("### " + constraintViolation.getRootBeanClass().getSimpleName() +
        "." + constraintViolation.getPropertyPath() + " - Invalid Value = " + constraintViolation.getInvalidValue() + " - Error Msg = " + constraintViolation.getMessage());

    }
  }
}
