package notesServer.dto.validator;

import notesServer.erros.ServerError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UserNameValidator implements ConstraintValidator<UserName, String> {

    @Value("50")
    private int maxNameLength;

    @Override
    public void initialize(UserName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if ((StringUtils.isEmpty(s)) || (s.length() > maxNameLength) || (!s.matches("^[a-zA-Zа-яА-Я\\s\\-]+$"))) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(ServerError.INVALID_USER_NAME.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
