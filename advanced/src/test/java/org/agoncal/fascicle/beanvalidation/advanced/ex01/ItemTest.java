package org.agoncal.fascicle.beanvalidation.advanced.ex01;

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
public class ItemTest {

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

    // Creates a book
    Item book = new Item(1L, "H2G2", 12.5f, "Best IT Scifi Book");

    // Validate the cd
    Set<ConstraintViolation<Item>> violations = validator.validate(book);
    assertEquals(0, violations.size());
  }

  @Test
  public void shouldRaiseConstraintsViolation() {

    // Creates a book
    Item book = new Item();

    // Validate the cd
    Set<ConstraintViolation<Item>> violations = validator.validate(book);
    displayConstraintViolations(violations);
    assertEquals(2, violations.size());
  }

  @Test
  public void shouldRaiseNoConstraintViolationOnCalculateVAT() throws NoSuchMethodException {

    Item item = new Item(1L, "H2G2", 12.5f, "Best IT Scifi Book");

    ExecutableValidator methodValidator = validator.forExecutables();
    Method method = Item.class.getMethod("calculateVAT");
    Set<ConstraintViolation<Item>> violations = methodValidator.validateReturnValue(item, method, new Float(10.0));
    assertEquals(0, violations.size());
  }

  @Test
  public void shouldRaiseNoConstraintViolationOnCalculatePrice() throws NoSuchMethodException {

    Item item = new Item(1L, "H2G2", 12.5f, "Best IT Scifi Book");

    ExecutableValidator methodValidator = validator.forExecutables();
    Method method = Item.class.getMethod("calculatePrice", Float.class);
    Set<ConstraintViolation<Item>> violations = methodValidator.validateParameters(item, method, new Object[]{new Float(4.5)});
    assertEquals(0, violations.size());
  }

  @Test
  public void shouldRaiseConstraintViolationOnCalculatePriceCauseRateIsTooLow() throws NoSuchMethodException {

    Item item = new Item(1L, "H2G2", 12.5f, "Best IT Scifi Book");

    ExecutableValidator methodValidator = validator.forExecutables();
    Method method = Item.class.getMethod("calculatePrice", Float.class);
    Set<ConstraintViolation<Item>> violations = methodValidator.validateParameters(item, method, new Object[]{new Float(0.5)});
    displayConstraintViolations(violations);
    assertEquals(1, violations.size());
  }

  private void displayConstraintViolations(Set<ConstraintViolation<Item>> constraintViolations) {
    for (ConstraintViolation constraintViolation : constraintViolations) {
      System.out.println("### " + constraintViolation.getRootBeanClass().getSimpleName() +
        "." + constraintViolation.getPropertyPath() + " - Invalid Value = " + constraintViolation.getInvalidValue() + " - Error Msg = " + constraintViolation.getMessage());

    }
  }
}
