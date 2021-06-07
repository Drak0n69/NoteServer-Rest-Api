package notesServer.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SectionNameValidator.class)
public @interface SectionName {
    String message() default "Incorrect section name!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
