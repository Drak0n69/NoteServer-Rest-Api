package notesServer.dto.response.exception;

import java.util.List;

public class ExceptionResponseDto {

    private List<ErrorResponseDto> errors;

    public ExceptionResponseDto(List<ErrorResponseDto> errors) {
        this.errors = errors;
    }

    public List<ErrorResponseDto> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorResponseDto> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ExceptionResponseDto{" +
                "errors=" + errors +
                '}';
    }
}
