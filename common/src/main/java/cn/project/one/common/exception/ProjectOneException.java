package cn.project.one.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectOneException extends RuntimeException {

    private int httpStatus;
    private String message;

    public ProjectOneException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ProjectOneException(String message, int httpStatus, String reason) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = reason;
    }

    public ProjectOneException(String message, Throwable cause, int httpStatus, String reason) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.message = reason;
    }

    public ProjectOneException(Throwable cause, int httpStatus, String message) {
        super(cause);
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ProjectOneException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
        int httpStatus, String reason) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
        this.message = reason;
    }
}