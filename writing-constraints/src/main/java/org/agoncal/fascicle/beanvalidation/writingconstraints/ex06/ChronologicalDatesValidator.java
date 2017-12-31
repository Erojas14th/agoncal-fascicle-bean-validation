package org.agoncal.fascicle.beanvalidation.writingconstraints.ex06;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Antonio Goncalves
 * http://www.antoniogoncalves.org
 * --
 */
public class ChronologicalDatesValidator implements ConstraintValidator<ChronologicalDates, Order> {

  @Override
  public void initialize(ChronologicalDates constraintAnnotation) {
  }

  @Override
  public boolean isValid(Order order, ConstraintValidatorContext context) {
    return order.getCreationDate().isBefore(order.getPaymentDate()) && order.getPaymentDate().isBefore(order.getDeliveryDate());
  }
}
