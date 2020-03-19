package com.wukong.provider.config.filter;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "loginFilter")
public class LoginFilter  implements Filter {
    /**
     * 容器加载的时候调用
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init loginFilter");
    }
    /**
     * 请求被拦截的时候进行调用
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter loginFilter");
        filterChain.doFilter(servletRequest,servletResponse);
    }
    /**
     * 容器被销毁的时候被调用
     */
    @Override
    public void destroy() {
        System.out.println("destroy loginFilter");
    }
}