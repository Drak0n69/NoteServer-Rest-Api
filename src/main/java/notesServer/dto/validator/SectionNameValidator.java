package notesServer.dto.validator;

import notesServer.erros.ServerError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionNameValidator implements ConstraintValidator<SectionName, String> {

    @Value("50")
    private int maxNameLength;

    @Override
    public void initialize(SectionName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if ((StringUtils.isEmpty(s)) || (s.length() > maxNameLength) || (!s.matches("^[a-zA-Z0-9а-яА-Я\\s\\-]+$"))) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(ServerError.INVALID_USER_NAME.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
