package ru.ath.asu.tickets.servlet.filter;

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

        log.warn(" ======== ");
        log.warn(" message from filter ");


        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

//        HttpSession session = httpServletRequest.getSession(false);// don't create if it doesn't exist
        HttpSession session = httpServletRequest.getSession(true);
//        if(session != null && !session.isNew()) {
        if ((session != null) && (!session.isNew())) {
            String sessUser = (String)session.getAttribute("user");
            String sessToken = (String)session.getAttribute("token");

            log.warn(" ======== ");
            log.warn(" sess user from filter:  " + sessUser);
            log.warn(" sess token from filter: " + sessToken);

            if ((sessUser == null) || (sessToken == null)) {

                ((HttpServletResponse)response).sendRedirect("portalAction!auth.jspa");
                return;
            }


            if (!sessUser.equals("alx") && sessToken.equals("123")) {
//                HttpServletResponse httpResponse =

                log.warn(" ======== ");
                log.warn(" unsuccessful attempt ");

                ((HttpServletResponse)response).sendRedirect("portalAction!auth.jspa");
                return;

            }

        } else {
            log.warn(" ======== ");
            log.warn(" session not found ");

            ((HttpServletResponse)response).sendRedirect("portalAction!auth.jspa");
            return;
        }

        //continue the request
        chain.doFilter(request,response);
    }

}