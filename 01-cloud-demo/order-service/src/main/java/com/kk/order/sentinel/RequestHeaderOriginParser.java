package com.kk.order.sentinel;


import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestHeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String origin = httpServletRequest.getHeader("origin");
        if(Strings.isEmpty(origin)) origin = "blank";
        System.out.println(origin);
        return origin;
    }
}
