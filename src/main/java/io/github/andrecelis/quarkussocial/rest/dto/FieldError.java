package io.github.andrecelis.quarkussocial.rest.dto;

public class FieldError {
    private String field;

    public String getMessage() {
        return message;
    }

    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
