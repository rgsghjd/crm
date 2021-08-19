package cg.software.web.filter;


import cg.software.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到登录拦截过滤器");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String path=request.getServletPath();
        if ("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
            filterChain.doFilter(request,response);
        }else{
            HttpSession session=request.getSession();
            User user=(User)session.getAttribute("User");
            if (user != null){
                filterChain.doFilter(request,response);
            }else {
                response.sendRedirect("/crm/login.jsp");
            }
        }


    }
}
