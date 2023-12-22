package cn.project.one.springboot.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.core.map.MapBuilder;
import cn.project.one.common.exception.ProjectOneException;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    @ExceptionHandler(ProjectOneException.class)
    public Object httpExceptionAdvice(ProjectOneException e) {
        return MapBuilder.create(true).put("code", e.getHttpStatus()).put("message", e.getMessage()).build();
    }
}
