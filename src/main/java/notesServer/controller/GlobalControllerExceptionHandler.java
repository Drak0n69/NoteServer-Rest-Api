package notesServer.controller;

import notesServer.dto.response.exception.ErrorResponseDto;
import notesServer.dto.response.exception.ExceptionResponseDto;
import notesServer.erros.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ControllerAdvice
@EnableWebMvc
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionResponseDto handleFieldValidation(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        List<FieldError> fields = ex.getBindingResult().getFieldErrors();
        List<ErrorResponseDto> errorDtoList = new ArrayList<>();
        for (int i = 0; i < errors.size(); i++) {
            String message = errors.get(i).getDefaultMessage();
            String field = fields.get(i).getField();
            errorDtoList.add(new ErrorResponseDto("INVALID_FIELD", field, message));
        }
        return new ExceptionResponseDto(errorDtoList);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ExceptionResponseDto handleFieldValidation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
        List<ErrorResponseDto> errorDtoList = new ArrayList<>();
        for (ConstraintViolation violation : errors) {
            String message = violation.getMessage();
            String field = violation.getPropertyPath().toString();
            field = field.substring(field.indexOf(".") + 1);
            errorDtoList.add(new ErrorResponseDto("INVALID_REQUEST_PARAM", field, message));
        }
        return new ExceptionResponseDto(errorDtoList);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ExceptionResponseDto handleFieldValidation(MethodArgumentTypeMismatchException ex) {
        ErrorResponseDto errorDto = new ErrorResponseDto("INVALID_URL", "URL", "Invalid URL variable!");
        return new ExceptionResponseDto(Collections.singletonList(errorDto));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerException.class)
    @ResponseBody
    public ExceptionResponseDto handleServerException(ServerException ex) {
        ErrorResponseDto errorDto = new ErrorResponseDto(ex.getServerError().name(), ex.getServerError().getField(),
                ex.getServerError().getMessage());
        return new ExceptionResponseDto(Collections.singletonList(errorDto));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ExceptionResponseDto handleNotFoundException() {
        ErrorResponseDto errorDto = new ErrorResponseDto("NOT_FOUND", "URL", "Invalid url!");
        return new ExceptionResponseDto(Collections.singletonList(errorDto));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ExceptionResponseDto handleBadJsonException(HttpMessageNotReadableException ex) {
        String message = "No JSON found!";
        Throwable exception = ex.getCause();
        if (exception != null) {
            message = exception.getMessage();
        }
        ErrorResponseDto errorDto = new ErrorResponseDto("INVALID_JSON", "Request Body", message);
        return new ExceptionResponseDto(Collections.singletonList(errorDto));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseBody
    public ExceptionResponseDto handleMissingCookieException() {
        ErrorResponseDto errorDto = new ErrorResponseDto("MISSING_COOKIE", "cookie", "Cookie required for this operation!");
        return new ExceptionResponseDto(Collections.singletonList(errorDto));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ExceptionResponseDto handleMethodNotSupportedException() {
        ErrorResponseDto errorDto = new ErrorResponseDto("INVALID_METHOD", "", "HTTP method is not supported for this URL!");
        return new ExceptionResponseDto(Collections.singletonList(errorDto));
    }
}