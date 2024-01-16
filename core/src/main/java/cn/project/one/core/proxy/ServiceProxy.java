package cn.project.one.core.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.project.one.api.annotation.*;
import cn.project.one.common.constants.ResultCodeEnum;
import cn.project.one.common.exception.ProjectOneException;
import cn.project.one.common.instance.Instance;
import cn.project.one.core.loadbalance.RandomLoadBalance;
import cn.project.one.core.instance.ServiceList;

/**
 * 代理执行类
 *
 * @since 2023/7/28
 */
public class ServiceProxy implements InvocationHandler {

    private static final String URL = "%s:%s%s";

    private static final int SUCCESS = 200;

    private final TimeInterval interval = new TimeInterval();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        interval.start();
        Feign feign = method.getDeclaringClass().getAnnotation(Feign.class);
        Mapping mapping = method.getAnnotation(Mapping.class);
        boolean responseBody = method.isAnnotationPresent(RespBody.class);
        Instance instance = RandomLoadBalance.getInstance().get(ServiceList.getInstance().getGroup(feign.service()));
        String uri = String.format(URL, instance.getAddress(), instance.getPort(), mapping.value());

        HttpRequest httpClient = HttpUtil.createRequest(mapping.method(), uri);

        String body = "";
        Map<String, String> formData = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        String respBody;
        if (ArrayUtil.isNotEmpty(args)) {
            body = body(method, args);
            formData = formData(method, args);
            headers = headers(method, args);
        }
        HttpResponse response = httpClient.body(body).formStr(formData).headerMap(headers, false).execute();
        if (response.getStatus() != SUCCESS) {
            if (response.getStatus() == ResultCodeEnum.NOT_FOUND.getCode()) {
                throw new ProjectOneException(response.getStatus(),
                    String.format(feign.service() + " service mapping " + mapping.value() + " not found. "));
            } else {
                throw new ProjectOneException(response.getStatus(),
                    String.format("call %s error, request : %s", uri, response.body()));
            }
        }

        if (responseBody) {
            return JSONUtil.toBean(response.body(), method.getReturnType());
        } else {
            respBody = response.body();
        }

        Console.log("Call [{} {}.{}] execute spend [{}]ms return value [{}]", feign.service(),
            method.getDeclaringClass().getSimpleName(), method.getName(), interval.intervalMs(), respBody);

        return respBody;
    }

    private String body(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (ArrayUtil.isEmpty(parameterAnnotations)) {
            return null;
        }

        String body;
        Map<String, Object> parameters = new HashMap<>();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (int j = 0; j < parameterAnnotation.length; j++) {
                if (parameterAnnotation[j] instanceof ReqBody) {
                    body = JSONUtil.toJsonStr(args[j]);
                    return body;
                }
            }
        }
        body = JSONUtil.toJsonStr(parameters);
        return body;
    }

    private Map<String, String> formData(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (ArrayUtil.isEmpty(parameterAnnotations)) {
            return null;
        }

        Map<String, String> parameters = new HashMap<>();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (int j = 0; j < parameterAnnotation.length; j++) {
                if (parameterAnnotation[j] instanceof Param) {
                    Param param = (Param)parameterAnnotation[j];
                    parameters.put(param.name(), StrUtil.toString(args[j]));
                }
            }
        }
        return parameters;
    }

    private Map<String, String> headers(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (ArrayUtil.isEmpty(parameterAnnotations)) {
            return null;
        }

        Map<String, String> head = new HashMap<>();

        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (int j = 0; j < parameterAnnotation.length; j++) {
                if (parameterAnnotation[j] instanceof Header) {
                    Header header = (Header)parameterAnnotation[j];
                    head.put(header.name(), args[j] == null ? header.defaultValue() : StrUtil.toString(args[j]));
                }
            }
        }
        return head;
    }
}
