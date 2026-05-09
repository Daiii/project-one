package cn.project.one.core.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.project.one.api.annotation.Feign;
import cn.project.one.api.annotation.Header;
import cn.project.one.api.annotation.Mapping;
import cn.project.one.api.annotation.Param;
import cn.project.one.api.annotation.ReqBody;
import cn.project.one.api.annotation.RespBody;
import cn.project.one.common.exception.ProjectOneException;
import cn.project.one.common.instance.Instance;
import cn.project.one.core.instance.ServiceList;
import cn.project.one.core.loadbalance.RandomLoadBalance;

/**
 * 代理执行类
 *
 * @since 2023/7/28
 */
public class ServiceProxy implements InvocationHandler {

    private static final String URL_FORMAT = "%s:%s%s";

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        TimeInterval interval = new TimeInterval();
        interval.start();

        Feign feign = method.getDeclaringClass().getAnnotation(Feign.class);
        Mapping mapping = method.getAnnotation(Mapping.class);
        Instance instance = RandomLoadBalance.getInstance().get(ServiceList.getInstance().getGroup(feign.service()));
        String uri = String.format(URL_FORMAT, instance.getAddress(), instance.getPort(), mapping.value());

        HttpRequest httpClient = HttpUtil.createRequest(mapping.method(), uri);
        RequestParams params = parseParams(method, args);

        HttpResponse response =
            httpClient.body(params.body).formStr(params.formData).headerMap(params.headers, false).execute();

        checkResponse(response, uri, feign, mapping);

        Object result = method.isAnnotationPresent(RespBody.class)
            ? JSONUtil.toBean(response.body(), method.getReturnType()) : response.body();

        Console.log("Call [{} {}.{}] execute spend [{}]ms return value [{}]", feign.service(),
            method.getDeclaringClass().getSimpleName(), method.getName(), interval.intervalMs(), result);

        return result;
    }

    private void checkResponse(HttpResponse response, String uri, Feign feign, Mapping mapping) {
        if (response.getStatus() == HttpStatus.OK.value()) {
            return;
        }
        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            throw new ProjectOneException(response.getStatus(),
                String.format("%s service mapping %s not found.", feign.service(), mapping.value()));
        }
        throw new ProjectOneException(response.getStatus(),
            String.format("call %s error, request: %s", uri, response.body()));
    }

    /**
     * 一次性解析方法参数注解，提取 body、formData、headers
     */
    private RequestParams parseParams(Method method, Object[] args) {
        String body = "";
        Map<String, String> formData = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        if (ArrayUtil.isNotEmpty(args)) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof ReqBody) {
                        body = JSONUtil.toJsonStr(args[i]);
                    } else if (annotation instanceof Param) {
                        formData.put(((Param)annotation).name(), StrUtil.toString(args[i]));
                    } else if (annotation instanceof Header) {
                        Header h = (Header)annotation;
                        headers.put(h.name(), args[i] == null ? h.defaultValue() : StrUtil.toString(args[i]));
                    }
                }
            }
        }
        return new RequestParams(body, formData, headers);
    }

    private static class RequestParams {
        final String body;
        final Map<String, String> formData;
        final Map<String, String> headers;

        RequestParams(String body, Map<String, String> formData, Map<String, String> headers) {
            this.body = body;
            this.formData = formData;
            this.headers = headers;
        }
    }
}
