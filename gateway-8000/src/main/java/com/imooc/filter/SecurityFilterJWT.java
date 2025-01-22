package com.imooc.filter;

import com.google.gson.Gson;
import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.jwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class SecurityFilterJWT implements GlobalFilter, Ordered {
    public static final String HEADER_USER_TOKEN = "headerUserToken";
    @Autowired
    private  ExcludeUrlProperties excludeUrlProperties;

    @Autowired
    private jwtUtils jwtUtils;


    // url path matcher
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // filter out, that not need to use JWT to validate

        // 1. get url path
        String path = exchange.getRequest().getURI().getPath();

        // 2, get exclude url list
        List<String> excludeUrls = excludeUrlProperties.getUrls();

        if(excludeUrls !=null && !excludeUrls.isEmpty()){
            for (String excludeUrl : excludeUrls) {
                if (antPathMatcher.matchStart(excludeUrl,path)){
                    return chain.filter(exchange);
                }
            }
        }
        log.info("被拦截了");
        // if not then need to get jwt validation
//        GraceException.display(ResponseStatusEnum.UN_LOGIN);
        HttpHeaders headers = exchange.getResponse().getHeaders();
        String userToken = headers.getFirst(HEADER_USER_TOKEN);

        if(StringUtils.isEmpty(userToken)){
            String[] tokenArr = userToken.split(jwtUtils.at);
            if(tokenArr.length<2){
                return renderErrorMsg(exchange,ResponseStatusEnum.UN_LOGIN);
            }
        }
        // 获得jwt令牌与前缀

        return renderErrorMsg(exchange,ResponseStatusEnum.UN_LOGIN);
    }

    /**
     * 重新包装并且返回错误信息
     * @param exchange
     * @param statusEnum
     * @return
     */
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum statusEnum){
        // 1.获得response
        ServerHttpResponse response = exchange.getResponse();

        // 2. 构建jsonResult
        GraceJSONResult jsonResult = GraceJSONResult.exception(statusEnum);

        // response code = 500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        // 设置header类型
        if(response.getHeaders().containsKey("Content-Type")){
            response.getHeaders().add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        }
        String resultJson = new Gson().toJson(jsonResult);
        DataBuffer wrap = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(wrap));
    }

    // 过滤器的顺序，数字越小优先级越大
    @Override
    public int getOrder() {
        return 0;
    }
}
