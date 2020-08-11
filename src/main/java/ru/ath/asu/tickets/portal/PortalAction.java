package ru.ath.asu.tickets.portal;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.ath.asu.tickets.aousers.TicketUser;
import ru.ath.asu.tickets.aousers.TicketUserDao;
import ru.ath.asu.tickets.auth.AuthTools;
import ru.ath.asu.tickets.auth.AuthUserInfo;
import ru.ath.asu.tickets.auth.UserInfo;
//import org.springframework.beans.factory.annotation.Qualifier;

//import javax.inject.Inject;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//@Named
public class PortalAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(PortalAction.class);

    private String userName;
    private final TicketUserDao ticketUserDao;

//    private AuthUserInfo authUserInfo;


//    @Inject
    public PortalAction(TicketUserDao ticketUserDao) {
        this.ticketUserDao = ticketUserDao;
    }

    @Override
    public String execute() throws Exception {

//        HttpServletRequest req = getHttpRequest();
//        HttpSession session = req.getSession();
//        String sessUser = (String)session.getAttribute("user");
//        String sessToken = (String)session.getAttribute("token");
//
//        log.warn(" ======== ");
//        log.warn(" sess user:  " + sessUser);
//        log.warn(" sess token: " + sessToken);

        return super.execute();
        //return "login";
    }

    public String doDefault() throws Exception {

        // используем метод для разрегистрации пользователя
        HttpServletRequest req = getHttpRequest();
        HttpSession session = req.getSession();

        session.invalidate();


        this.userName = "";
        return "login";
    }

    public String doAuth() throws Exception {

        // для входа нужно будет ввести в броузер url вида
        // localhost:2990/jira/secure/portalAction!auth.jspa?login-name=tvv@kiravto.ru&login-pass=28cGJ7qhwD

        String loginName = getHttpRequest().getParameter("login-name");
        String loginPass = getHttpRequest().getParameter("login-pass");

        if (loginName == null) {
            loginName = "";
        }

        if (loginPass == null) {
            loginPass = "";
        }


        this.userName = loginName;


        // проверка логина пароля
        UserInfo userInfo = tryAuthenticate(loginName, loginPass);
        if ( userInfo != null) {

            HttpServletRequest req = getHttpRequest();
            HttpSession session = req.getSession();
            session.setAttribute("user", userInfo.getId());
            session.setAttribute("token", userInfo.getToken());

            // тут подцепим к сессии имя и подразделение
            session.setAttribute("fio", userInfo.getFio());
            session.setAttribute("department", userInfo.getDepartment());


            log.warn(" ===== авторизовался");
            return getRedirect("ordersAction!menu.jspa");
//            return "menu";
        }

        log.warn(" ===== не авторизовался");
        return "login";
    }


    // попытка авторизации
    public UserInfo tryAuthenticate(String login, String password) {
        // проверка дао
        TicketUser ticketUser = ticketUserDao.findByLoginPassword(login, password);

        if (ticketUser != null) {
            // запишем токен в тикет юзер
            ticketUser.setToken(AuthTools.generateToken());
            ticketUserDao.update(ticketUser);

            return AuthTools.getUserInfoFromTicketUser(ticketUser);
        }

        return null;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
