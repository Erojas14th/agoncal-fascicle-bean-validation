package org.agoncal.fascicle.beanvalidation.writingconstraints.ex07;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * @author Antonio Goncalves
 * http://www.antoniogoncalves.org
 * --
 */
@Pattern(regexp = "[A-Z][a-z]{1,}")
@Size(min = 3, max = 20)
@ReportAsSingleViolation
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MusicGenre {

  String message() default "{music.genre}";

  Class<? extends Payload>[] payload() default {};

  Class<?>[] groups() default {};
}
