package ru.ath.asu.servlet.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckSessionAuth implements Filter{
    private static final Logger log = LoggerFactory.getLogger(CheckSessionAuth.class);

    public void init(FilterConfig filterConfig)throws ServletException{
    }

    public void destroy(){
    }


    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)throws IOException,ServletException{
        //do some custom handling here

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        HttpSession session = httpServletRequest.getSession(false);// don't create if it doesn't exist
//        if(session != null && !session.isNew()) {
        if(session != null) {

            String sessUser = (String)session.getAttribute("user");
            String sessToken = (String)session.getAttribute("token");

            log.warn(" ======== ");
            log.warn(" sess user from filter:  " + sessUser);
            log.warn(" sess token from filter: " + sessToken);
//
//        }
//            response.sendRedirect("/login.jsp");
        }

        //continue the request
        chain.doFilter(request,response);
    }

}