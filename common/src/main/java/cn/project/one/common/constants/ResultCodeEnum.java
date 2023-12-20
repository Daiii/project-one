package cn.project.one.common.constants;

public enum ResultCodeEnum {

    OK(0, ""), SUCCESS(200, ""), NOT_FOUND(404, "Not Found");

    private int code;
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
