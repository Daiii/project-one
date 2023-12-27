package cn.project.one.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    OK(0, ""), SUCCESS(200, ""), NOT_FOUND(404, "Not Found");

    private final int code;
    private final String message;
}
