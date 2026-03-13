package top.kagg886.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.kagg886.backend.dto.BaseResponse;

@Slf4j
@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public ResponseWrapperAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getContainingClass().getName().startsWith("top.kagg886.backend");
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            return objectMapper.writeValueAsString(BaseResponse.ok(body));
        }
        if (body instanceof BaseResponse<?> instance) {
            if (instance.getData() instanceof BaseResponse) {
                return beforeBodyWrite(instance.getData(), returnType, selectedContentType, selectedConverterType, request, response);
            }
            return instance;
        }
        return BaseResponse.ok(body);
    }

    @ExceptionHandler(Throwable.class)
    public BaseResponse<?> handleException(Throwable e) {
        if (e instanceof BaseResponse.Wrapped wrapped) {
            return BaseResponse.error(wrapped.getCode(), wrapped.getMessage());
        }
        log.error("未知错误", e);
        return BaseResponse.error(-1, e.getMessage());
    }
}
