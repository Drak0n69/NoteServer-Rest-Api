package notesServer.dto.validator;

import notesServer.erros.ServerError;
import notesServer.util.Settings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Value("10")
    private int minPasswordLength;

    @Override
    public void initialize(Password constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if ((StringUtils.isEmpty(s)) || (s.length() < minPasswordLength)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(ServerError.INVALID_USER_PASSWORD.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
