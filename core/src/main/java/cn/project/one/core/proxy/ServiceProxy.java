package cn.project.one.core.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
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
import cn.project.one.common.instance.Instance;
import cn.project.one.core.loadbalance.RandomLoadBalance;
import cn.project.one.core.service.ServiceList;

/**
 * 代理执行类
 * 
 * @since 2023/7/28
 */
public class ServiceProxy implements InvocationHandler {

    private static final String URL = "%s:%s%s";

    private static final int SUCCESS = 200;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Feign feign = method.getDeclaringClass().getAnnotation(Feign.class);
        Mapping mapping = method.getAnnotation(Mapping.class);
        boolean responseBody = method.isAnnotationPresent(RespBody.class);
        Instance instance = RandomLoadBalance.getInstance().get(ServiceList.getInstance().getGroup(feign.service()));
        String uri = String.format(URL, instance.getAddress(), instance.getPort(), mapping.value());

        HttpRequest request = HttpUtil.createRequest(mapping.method(), uri);
        String reqBody = "";
        String respBody;
        Map<String, String> headers = new HashMap<>();
        if (ArrayUtil.isNotEmpty(args)) {
            reqBody = generateRequest(method, args, headers);
        }
        HttpResponse response = request.body(reqBody).addHeaders(headers).executeAsync();
        if (response.getStatus() != SUCCESS) {
            throw new HttpException(
                String.format("call %s exception request = %s, response = %s", uri, request, response));
        }

        if (responseBody) {
            return JSONUtil.toBean(response.body(), method.getReturnType());
        } else {
            respBody = response.body();
        }

        return respBody;
    }

    private String generateRequest(Method method, Object[] args, Map<String, String> headers) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (ArrayUtil.isEmpty(parameterAnnotations)) {
            return null;
        }

        String body;
        Map<String, Object> parameters = new HashMap<>();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (int j = 0; j < parameterAnnotation.length; j++) {
                if (parameterAnnotation[j] instanceof Param) {
                    Param param = (Param)parameterAnnotation[j];
                    parameters.put(param.name(), args[j]);
                }
                if (parameterAnnotation[j] instanceof Header) {
                    Header header = (Header)parameterAnnotation[j];
                    headers.put(header.name(), args[j] == null ? header.defaultValue() : StrUtil.toString(args[j]));
                }
                if (parameterAnnotation[j] instanceof ReqBody) {
                    body = JSONUtil.toJsonStr(args[j]);
                    return body;
                }
            }
        }
        body = JSONUtil.toJsonStr(parameters);
        return body;
    }
}
