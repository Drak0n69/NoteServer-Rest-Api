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
public class LoginValidator implements ConstraintValidator<Login, String> {

    @Value("50")
    private int maxNameLength;

    @Override
    public void initialize(Login constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if ((StringUtils.isEmpty(s)) || (s.length() > maxNameLength) || (!s.matches("^[a-zA-Z0-9а-яА-Я]+$"))) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(ServerError.INVALID_USER_LOGIN.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
