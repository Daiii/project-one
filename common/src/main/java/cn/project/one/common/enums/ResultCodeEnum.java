package cn.project.one.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    OK(0, "Ok"),
    SUCCESS(200, "Success"),
    NOT_FOUND(404, "Not Found");

    private final int code;
    private final String message;
}
