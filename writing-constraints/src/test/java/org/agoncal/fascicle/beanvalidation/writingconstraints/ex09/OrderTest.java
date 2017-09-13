package org.agoncal.fascicle.beanvalidation.writingconstraints.ex09;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Antonio Goncalves
 * http://www.antoniogoncalves.org
 * --
 */
public class OrderTest {

  // ======================================
  // =             Attributes             =
  // ======================================

  protected static ValidatorFactory vf;
  protected static Validator validator;

  private static Date creationDate;
  private static Date paymentDate;
  private static Date deliveryDate;


  // ======================================
  // =          Lifecycle Methods         =
  // ======================================

  @BeforeAll
  public static void init() throws ParseException {
    vf = Validation.buildDefaultValidatorFactory();
    validator = vf.getValidator();

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    creationDate = dateFormat.parse("01/01/2010");
    paymentDate = dateFormat.parse("02/01/2010");
    deliveryDate = dateFormat.parse("03/01/2010");
  }

  @AfterAll
  public static void close() {
    vf.close();
  }

  // ======================================
  // =              Methods               =
  // ======================================

  @Test
  public void shouldRaiseNoConstraintsViolation() {

    Order order = new Order();
    order.setId(1234L);
    order.setTotalAmount(1234.5);

    order.setCreationDate(creationDate);

    Set<ConstraintViolation<Order>> violations = validator.validate(order);
    displayContraintViolations(violations);
    assertEquals(0, violations.size());

    order.setPaymentDate(paymentDate);

    violations = validator.validate(order, Payment.class);
    displayContraintViolations(violations);
    assertEquals(0, violations.size());

    order.setDeliveryDate(deliveryDate);

    violations = validator.validate(order, Delivery.class);
    assertEquals(0, violations.size());
  }

  @Test
  public void shouldRaiseConstraintsViolationCauseDatesAreNotChronological() {

    Order order = new Order();
    order.setId(1234L);
    order.setTotalAmount(1234.5);

    order.setCreationDate(creationDate);

    Set<ConstraintViolation<Order>> violations = validator.validate(order);
    assertEquals(0, violations.size());

    order.setPaymentDate(creationDate);

    violations = validator.validate(order, Payment.class);
    assertEquals(0, violations.size());

    order.setDeliveryDate(creationDate);

    violations = validator.validate(order, Delivery.class);
    assertEquals(1, violations.size());
  }

  private void displayContraintViolations(Set<ConstraintViolation<Order>> constraintViolations) {
    for (ConstraintViolation constraintViolation : constraintViolations) {
      System.out.println("### " + constraintViolation.getRootBeanClass().getSimpleName() +
        "." + constraintViolation.getPropertyPath() + " - Invalid Value = " + constraintViolation.getInvalidValue() + " - Error Msg = " + constraintViolation.getMessage());

    }
  }
}
