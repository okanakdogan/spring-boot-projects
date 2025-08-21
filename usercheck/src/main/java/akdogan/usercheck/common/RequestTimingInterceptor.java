package akdogan.usercheck.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestTimingInterceptor implements HandlerInterceptor{
    
    private static final String START_TIME = "startTime";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute(START_TIME,System.currentTimeMillis());
        return true;
    }

    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        Long startTime = (Long)request.getAttribute(START_TIME);
        if(startTime != null){
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("[Profiler] " + request.getMethod() + " " + request.getRequestURI() + " took " + duration + " ms");
        }
        
    }

}
